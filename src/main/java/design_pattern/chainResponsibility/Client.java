package design_pattern.chainResponsibility;

import design_pattern.chainResponsibility.approver.CollegeApprover;
import design_pattern.chainResponsibility.approver.DepartmentApprover;
import design_pattern.chainResponsibility.approver.SchoolMasterApprover;
import design_pattern.chainResponsibility.approver.ViceSchoolMasterApprover;
/**
 * 职责链模式：使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系，将这个对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。
 *
 * Handler
 *
 * 解决实例需求：
 *   1. 编写程序完成学校OA系统的采购审批项目，需求如下：
 *       采购员采购教学器材，
 *          如果金额<=5000, 由教学主任审批
 *          如果金额<=10000,由院长审批
 *          如果金额<=30000，由副校长审批
 *          如果>30000,由校长审批
 *
 *
 *  Spring中的应用：
 *     DispatcherServlet -> HandlerExecutionChainmappedHandler
 */
public class Client {
    public static void main(String[] args) {
        PurchaseRequest purchaseRequest = new PurchaseRequest(1,21000,1);
        //创建相关的审批人
        DepartmentApprover departmentApprover = new DepartmentApprover("Mr Zhang");
        CollegeApprover collegeApprover = new CollegeApprover("Mr Li");
        ViceSchoolMasterApprover viceSchoolMasterApprover = new ViceSchoolMasterApprover("Wang Vice Master");
        SchoolMasterApprover schoolMasterApprover=new SchoolMasterApprover("Tong Master");

        //设置每一个审批人的下一个审批环节
        /**
         * 注意：所有处理人要形成环状：
         */
        departmentApprover.setNextApprover(collegeApprover);
        collegeApprover.setNextApprover(viceSchoolMasterApprover);
        viceSchoolMasterApprover.setNextApprover(schoolMasterApprover);
        schoolMasterApprover.setNextApprover(departmentApprover);

        schoolMasterApprover.processRequest(purchaseRequest);
    }
}
