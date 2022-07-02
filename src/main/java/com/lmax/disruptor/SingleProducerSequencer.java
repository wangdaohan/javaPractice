/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmax.disruptor;

import java.util.concurrent.locks.LockSupport;

import com.lmax.disruptor.util.Util;

abstract class SingleProducerSequencerPad extends AbstractSequencer
{
    protected long p1, p2, p3, p4, p5, p6, p7;
    public SingleProducerSequencerPad(int bufferSize, WaitStrategy waitStrategy)
    {
        super(bufferSize, waitStrategy);
    }
}

abstract class SingleProducerSequencerFields extends SingleProducerSequencerPad
{
    public SingleProducerSequencerFields(int bufferSize, WaitStrategy waitStrategy)
    {
        super(bufferSize, waitStrategy);
    }

    /** Set to -1 as sequence starting point */
    protected long nextValue = Sequence.INITIAL_VALUE;
    protected long cachedValue = Sequence.INITIAL_VALUE;
}

/**
 * <p>Coordinator for claiming sequences for access to a data structure while tracking dependent {@link Sequence}s.
 * Not safe for use from multiple threads as it does not implement any barriers.</p>
 *
 * <p>Note on {@link Sequencer#getCursor()}:  With this sequencer the cursor value is updated after the call
 * to {@link Sequencer#publish(long)} is made.
 */

public final class SingleProducerSequencer extends SingleProducerSequencerFields
{
    protected long p1, p2, p3, p4, p5, p6, p7;

    /**
     * Construct a Sequencer with the selected wait strategy and buffer size.
     *
     * @param bufferSize the size of the buffer that this will sequence over.
     * @param waitStrategy for those waiting on sequences.
     */
    public SingleProducerSequencer(int bufferSize, final WaitStrategy waitStrategy)
    {
        super(bufferSize, waitStrategy);
    }

    /**
     * @see Sequencer#hasAvailableCapacity(int)
     */
    @Override
    public boolean hasAvailableCapacity(final int requiredCapacity)
    {
        long nextValue = this.nextValue;

        long wrapPoint = (nextValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = this.cachedValue;

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue)
        {
            long minSequence = Util.getMinimumSequence(gatingSequences, nextValue);
            this.cachedValue = minSequence;

            if (wrapPoint > minSequence)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * @see Sequencer#next()
     */
    @Override
    public long next()
    {
        return next(1);
    }

    /**
     * @see Sequencer#next(int)
     */
    @Override
    public long next(int n)  // next(1) -> n = 1
    {
        // sequence初始值（即nextValue初始值）是-1
        // nextValue + n 为新的sequence值
        if (n < 1)
        {
            throw new IllegalArgumentException("n must be > 0");
        }

        long nextValue = this.nextValue; // 语义级别变量 - nextValue为SingleProducerSequencer的变量

        // nextSequence = -1 + 1; ->  0
        long nextSequence = nextValue + n;

        /**
         * wrapPoint是用来判断当前产生的新生产者的序号是否超出了ringBuffer长度，>0为超出， <=0,未超出
        // bufferSize为RingBuffer长度,假设为10 -》 wrapPoint = 0 - 10 = -10
        long wrapPoint = nextSequence - bufferSize;
        /**
         * cachedValue初始值也是-1
         * cachedGatingSequence -> 未知用法，语义上来说是用于缓存优化
         */
        long cachedGatingSequence = this.cachedValue;

        /**
         *  wrapPoint > cachedGatingSequence ??
         */
        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue)
        {
            long minSequence; //最小消费者的序号  －＞满足：c. 生产者序号数值不能大于消费者中最小的序号数值
            //自旋操作
            /**
             * 本段代码：Util.getMinimumSequence(gatingSequences, nextValue) -> 用于找到消费者中的最小序号值
             *
             * 本段逻辑：
             *     如果你的生产者序号大于消费者中最小的序号，那么就挂起并自旋操作
             *
             * 以满足：c. 生产者序号数值不能大于消费者中最小的序号数值
             */
            while (wrapPoint > (minSequence = Util.getMinimumSequence(gatingSequences, nextValue)))
            {
                LockSupport.parkNanos(1L); // 挂起线程 - TODO: Use waitStrategy to spin?
            }
            //cachedValue接收了最小的消费者序号
            this.cachedValue = minSequence;
        }

        this.nextValue = nextSequence;

        return nextSequence;
    }

    /**
     * @see Sequencer#tryNext()
     */
    @Override
    public long tryNext() throws InsufficientCapacityException
    {
        return tryNext(1);
    }

    /**
     * @see Sequencer#tryNext(int)
     */
    @Override
    public long tryNext(int n) throws InsufficientCapacityException
    {
        if (n < 1)
        {
            throw new IllegalArgumentException("n must be > 0");
        }

        if (!hasAvailableCapacity(n))
        {
            throw InsufficientCapacityException.INSTANCE;
        }

        long nextSequence = this.nextValue += n;

        return nextSequence;
    }

    /**
     * @see Sequencer#remainingCapacity()
     */
    @Override
    public long remainingCapacity()
    {
        long nextValue = this.nextValue;

        long consumed = Util.getMinimumSequence(gatingSequences, nextValue);
        long produced = nextValue;
        return getBufferSize() - (produced - consumed);
    }

    /**
     * @see Sequencer#claim(long)
     */
    @Override
    public void claim(long sequence)
    {
        this.nextValue = sequence;
    }

    /**
     * @see Sequencer#publish(long)
     */
    @Override
    public void publish(long sequence)
    {
        cursor.set(sequence);
        waitStrategy.signalAllWhenBlocking();
    }

    /**
     * @see Sequencer#publish(long, long)
     */
    @Override
    public void publish(long lo, long hi)
    {
        publish(hi);
    }

    /**
     * @see Sequencer#isAvailable(long)
     */
    @Override
    public boolean isAvailable(long sequence)
    {
        return sequence <= cursor.get();
    }

    @Override
    public long getHighestPublishedSequence(long lowerBound, long availableSequence)
    {
        return availableSequence;
    }
}
