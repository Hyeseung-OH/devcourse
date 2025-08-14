public class Test {
    public static void main(String[] args) {

        아빠 a사람 = new 사람();

        a사람.아기랑놀아주기();
        a사람.아기목욕();

        의사 a의사 = new 사람();

        a의사.수술();
        a의사.진단();


    }
}

interface 동물 {

    void 울다();
    void 숨쉬다();
}

interface 반려동물 {
}
// 상속 ==> 다중 상속 X
//class 개 extends 동물 {
//
//}

interface 아빠 {
    void 아기목욕();
    void 아기랑놀아주기();
}

interface 의사 {
    void 진단();
    void 수술();
    void 처방();
}

class 사람 implements 아빠, 의사 {
    public void 아기목욕() {
        System.out.println("아기를 목욕시킵니다.");
    }
    public void 아기랑놀아주기() {
        System.out.println("아기랑 놀아줍니다.");
    }

    @Override
    public void 진단() {
        System.out.println("환자를 진단합니다.");
    }

    @Override
    public void 수술() {
        System.out.println("환자를 수술합니다.");
    }

    @Override
    public void 처방() {
        System.out.println("환자에게 약을 처방합니다.");
    }

}

class 개 implements 동물, 반려동물 {
    public void 울다() {
        System.out.println("왈왈");
    }

    public void 숨쉬다() {
        System.out.println("헥헥");
    }
}

class 고양이 implements 동물 {
    public void 울다() {
        System.out.println("야옹");
    }

    public void 숨쉬다() {
        System.out.println("고로롱");
    }
}