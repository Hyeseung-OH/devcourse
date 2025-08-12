package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;
import com.back.system.SystemController;

import java.util.Scanner;

public class AppContext {
    public static Scanner sc;
    public static SystemController systemController;
    public static WiseSayingController wiseSayingController;
    public static WiseSayingService wiseSayingService;
    public static WiseSayingRepository wiseSayingRepository;

    public static void init(Scanner sc) {
        init();
        AppContext.sc = sc;
    }
    public static void init() {
        Scanner sc = new Scanner(System.in);
        AppContext.wiseSayingRepository = new WiseSayingRepository();
        AppContext.wiseSayingService = new WiseSayingService();
        AppContext.systemController = new SystemController();
        AppContext.wiseSayingController = new WiseSayingController();
    }

}
