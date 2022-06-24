package design_pattern.visitor;

public class FailAction extends Action {
    @Override
    public void getManResult(Man man) {
        System.out.println("男性给的评价是失败");
    }

    @Override
    public void getWomanResult(Woman woman) {
        System.out.println("女性给的评价是失败");
    }
}
