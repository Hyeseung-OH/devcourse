package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingFileRepository;
import com.back.domain.wiseSaying.repository.WiseSayingMemoryRepository;
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
    public static WiseSayingMemoryRepository wiseSayingMemoryRepository;

    public static void init(Scanner _sc, boolean isFileMode) {
        AppContext.sc = _sc;
        AppContext.wiseSayingMemoryRepository = new WiseSayingMemoryRepository();
        AppContext.wiseSayingFileRepository = new WiseSayingFileRepository();
        AppContext.wiseSayingRepository = isFileMode ? new WiseSayingFileRepository() : new WiseSayingMemoryRepository();
        AppContext.wiseSayingService = new WiseSayingService();
        AppContext.wiseSayingController = new WiseSayingController();
        AppContext.systemController = new SystemController();
    }

    public static void init() {
        init(new Scanner(System.in), true);
    }
}
