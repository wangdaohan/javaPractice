package design_pattern.chainResponsibility.approver;

import design_pattern.chainResponsibility.PurchaseRequest;

public class SchoolMasterApprover extends Approver {

    public SchoolMasterApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest purchaseRequest) {
         if(purchaseRequest.getPrice() > 30000){
             System.out.println("request id"+purchaseRequest.getId()+" is handled by "+this.name);
         }else{
             nextApprover.processRequest(purchaseRequest);
         }
    }
}
