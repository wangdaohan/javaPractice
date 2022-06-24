package design_pattern.visitor;


import java.util.LinkedList;
import java.util.List;

public class ObjectStructure{

    private List<Person> persons = new LinkedList<>();

    public void attach(Person p){
        persons.add(p);
    }

    public void detach(Person p ){
        persons.remove(p);
    }

    //显示测评情况
    public void display(Action action){
        for(Person p : persons){
            p.accept(action);
        }
    }
}
