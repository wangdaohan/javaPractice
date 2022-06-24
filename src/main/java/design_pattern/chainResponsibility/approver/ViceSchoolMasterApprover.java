package design_pattern.chainResponsibility.approver;

import design_pattern.chainResponsibility.PurchaseRequest;

public class ViceSchoolMasterApprover extends Approver {

    public ViceSchoolMasterApprover(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest purchaseRequest) {

        if(purchaseRequest.getPrice() > 10000 && purchaseRequest.getPrice()<=30000){
             System.out.println("request id"+purchaseRequest.getId()+" is handled by "+this.name);
         }else{
             nextApprover.processRequest(purchaseRequest);
         }
    }
}
