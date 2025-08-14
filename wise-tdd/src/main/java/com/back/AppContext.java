package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingFileRepository;
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
    public static WiseSayingFileRepository wiseSayingFileRepository;
    public static void init(Scanner _sc) {
        AppContext.sc = _sc;
        AppContext.wiseSayingRepository = new WiseSayingRepository();
        AppContext.wiseSayingService = new WiseSayingService();
        AppContext.wiseSayingController = new WiseSayingController();
        AppContext.systemController = new SystemController();
        AppContext.wiseSayingFileRepository = new WiseSayingFileRepository();
    }

    public static void init() {
        init(new Scanner(System.in));
    }

}
