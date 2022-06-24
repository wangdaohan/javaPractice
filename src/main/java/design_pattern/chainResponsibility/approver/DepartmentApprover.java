package design_pattern.chainResponsibility.approver;

import design_pattern.chainResponsibility.PurchaseRequest;

public class DepartmentApprover extends Approver {

    public DepartmentApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest purchaseRequest) {
         if(purchaseRequest.getPrice() <= 5000){
             System.out.println("request id"+purchaseRequest.getId()+" is handled by "+this.name);
         }else{
             nextApprover.processRequest(purchaseRequest);
         }
    }
}
