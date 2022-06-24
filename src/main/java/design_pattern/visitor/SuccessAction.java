package design_pattern.visitor;

public class SuccessAction extends Action {

    @Override
    public void getManResult(Man man) {
        System.out.println("男性给的评价是成功");
    }

    @Override
    public void getWomanResult(Woman woman) {
        System.out.println("女性给的评价是成功");
    }
}
