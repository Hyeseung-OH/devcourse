package controller;

import org.example.Rq;
import org.example.WiseSaying;
import service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {

    private Scanner sc;
    WiseSayingService wiseSayingService = new WiseSayingService();

    public WiseSayingController(Scanner sc) {
        this.sc = sc;
    }

    public void actionModify(Rq rq) {

        int id = rq.getParamAsInt("id", -1);
        WiseSaying wiseSaying = wiseSayingService.getByIdOrNull(id);

        if(wiseSaying == null) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.println("명언(기존) : %s".formatted(wiseSaying.getSaying()));
        System.out.print("명언 : ");
        String newSaying = sc.nextLine();
        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
        System.out.print("작가 : ");
        String newAuthor = sc.nextLine();

        wiseSayingService.modify(wiseSaying, newSaying, newAuthor);
    }


    public void actionDelete(Rq rq) {

        int id = rq.getParamAsInt("id", -1);
        boolean result = wiseSayingService.delete(id);

        if (result) {
            System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
        } else {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
        }
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayings = wiseSayingService.findListDesc();

        for (WiseSaying wiseSaying : wiseSayings) {
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getSaying(), wiseSaying.getAuthor()));
        }
    }


    public void actionWrite() {
        System.out.print("명언 : ");
        String saying = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = wiseSayingService.write(saying, author);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }
}