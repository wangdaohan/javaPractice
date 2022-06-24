package design_pattern.visitor;

/**
 * 这里我们使用到了双分派，即首先在客户端程序中，将具体状态作为参数传递woman中
 * 然后woman类调用 作为参数的"具体方法中getWomanResult,同时将自己作为参数传入 ，完成第二次的分派
 */
public class Woman extends Person{
    @Override
    public void accept(Action action) {
        action.getWomanResult(this);
    }
}
