package design_pattern.chainResponsibility.approver;

import design_pattern.chainResponsibility.PurchaseRequest;

public abstract class Approver {
    Approver nextApprover;

    String name;


    //处理审批请求的方法
    public abstract void processRequest(PurchaseRequest purchaseRequest);



    public Approver(String name) {
        this.name = name;
    }

    public Approver getNextApprover() {
        return nextApprover;
    }

    public void setNextApprover(Approver nextApprover) {
        this.nextApprover = nextApprover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
