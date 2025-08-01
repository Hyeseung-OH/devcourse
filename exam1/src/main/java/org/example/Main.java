package org.example;

public class Main {
    public static void main(String[] args) {
        int[] arr = {11, 22, 33, 44, 55, 66, 77, 88, 99, 100};

        for(int i = arr.length - 1; i >= 0; i--){
            if(arr[i] % 2 == 0) {
                System.out.println(arr[i]);
            }
        }
    }
}