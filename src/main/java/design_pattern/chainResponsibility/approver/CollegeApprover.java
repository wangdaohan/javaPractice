package design_pattern.chainResponsibility.approver;

import design_pattern.chainResponsibility.PurchaseRequest;

public class CollegeApprover extends Approver {

    public CollegeApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest purchaseRequest) {
         if(purchaseRequest.getPrice() > 5000 && purchaseRequest.getPrice()<=10000){
             System.out.println("request id"+purchaseRequest.getId()+" is handled by "+this.name);
         }else{
             nextApprover.processRequest(purchaseRequest);
         }
    }
}
