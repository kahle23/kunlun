package artoria.entity;

public class Superman extends Person implements FlyAbility {

    @Override
    public void fly() {
        System.out.println(this.getName() + " are fly now. ");
    }

}
