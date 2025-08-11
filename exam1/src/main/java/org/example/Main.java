package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Main {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person(1, "Alice", 20, 'F'));
        people.add(new Person(2, "Bob", 25, 'M'));
        people.add(new Person(3, "David", 35, 'M'));

        // 아래처럼 작성하면 비어 있는 객체를 target에 저장하게 되고
        // 이로 인해 NullPointerException 에러를 발생시킬 확률이 높음
//        Person target = people.stream()
//                .filter(p -> p.getId() == 2)
//                .findFirst();

        // Optional<> == <> 객체에 내용이 있을 수도 있고, 없을 수도 있다 = 비어 있을 수 있다
        // 상자라고 생각하면 편함 => 상자 안에 값이 있을 수도, 없을 수도
        Optional<Person> target = people.stream()
                .filter(p -> p.getId() == 2)
                .findFirst();

        // Optional 제공 메서드 => isEmpty() -> 비어 있습니까? , isPresent() -> 존재합니까?
        if(target.isPresent()) {
            // 상자에서 값을 꺼내야 함
            Person realTarget = target.get();
            System.out.println(realTarget.getName());
        }

        people.stream()
                .filter(p -> p.getId() == 2)
                .findFirst()
                .orElse(new Person(1, "Gildong", 24, 'M'));
        // .orElseThrow() -> 예외 발생 시 아예 던진다, 종료시킨다
    }
}


class Person {
    private int id;
    private String name;
    private int age;
    private char gender;

    public Person(int id, String name, int age, char gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public char getGender() {
        return gender;
    }
}