package de.mindcollaps.yuki.console.terminal;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;

import java.util.Scanner;

@YukiLogModule(name = "Terminal Command Handler")
public class YukiTerminalCommandHandlerThread implements Runnable {

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        YukiLogger.log(new YukiLogInfo("Console Command Handler initialized!").debug());
        while (true) {
            try {
                line = scanner.nextLine();
            } catch (Exception ignored) {
            }
            try {
                String answer = YukiTerminalCommandHandler.handleCommand(line);
                YukiLogger.log(new YukiLogInfo(answer));
            } catch (Exception e) {
                YukiLogger.log(new YukiLogInfo("An error occurred while executing the command!").trace(e));
            }
        }
    }
}