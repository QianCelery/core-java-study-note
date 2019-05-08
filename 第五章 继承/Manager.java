public class Manager extends Employee{
	...
	public double getSalary{
		...
	}
}

public double getSalary{
	return salary + bonus; // won't work
}

public double getSalaryO{
	double baseSalary = getSalary；// still won't work，无限调用自身正在实现的getSalary方法
	return baseSalary + bonus;
}

public double getSalaryO{
	double baseSalary = super.getSalary
	return baseSalary + bonus;
}

public Manager(String name, double salary, int year, int month, int day){
	super(name, salary, year, month, day) ;
	bonus = 0;
}


public class Employee{

	public boolean equals(Object otherObject){
		// a quick test to see if the objects are identical
		if (this == otherObject) return true;
		// must return false if the explicit parameter is null
		if (otherObject == null) return false;
		// if the classes don't match, they can't be equal
		if (getClassO != otherObject.getClass())
		return false;
		// now we know otherObject is a non-null Employee
		Employee other = (Employee) otherObject;
		// test whether the fields have identical values
		return name.equals(other.name)
		&& salary = other , sal ary
		&& hireDay. equals(other, hireDay) 
	}
}

return Objects.equals(name, other.name)
&& salary == other.sal ary
&& Object.equals(hireDay, other.hireDay) ;

public class Manager extends Employee{

	public boolean equals(Object otherObject){
	if (!super.equals(otherObject)) return false;
	// super.equals checked that this and otherObject belong to the same class
	Manager other = (Manager) otherObject;
	return bonus == other.bonus;
	}
}

public class Employee{
	public boolean equals(Employee other)
	{
		return other != null
		&& getClassO == other.getClass0
		&& Objects.equals(name, other. name)
		&& salary
		&& Objects—.equals other(hireDay , sal ary , other.hireDay
	}
}

//hashCode

public class Employee{
	public int hashCode(){
	return 7 * name.hashCode0
	+ 11 * new Double(salary).hashCode0
	+ 13 * hireDay.hashCode();
	}
}

public int hashCode(){
	return 7 * Objects.hashCode(name)
	+ 11 * Double.hashCode(salary)
	+ 13 * Objects.hashCode(hireDay);
}

//toString
public String toStringO{
	return getClass().getName()"[name=" + name
	+ ",salary:" + salary
	+ "，hireDay=" + hireDay;
}

public class Manager extends Employee{

	public String toStringO{
		return super.toString()
		+ "[bonus=" + bonus
		+ "]";
	}
}

//参数可变
public static double max(double... values){
	double largest = Double.NECATIVEJNFINITY;
	for (double v : values) if (v > largest) largest = v;
	return largest;
}