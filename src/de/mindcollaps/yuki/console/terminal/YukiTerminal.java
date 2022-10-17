package de.mindcollaps.yuki.console.terminal;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.console.terminal.commands.TCList;
import de.mindcollaps.yuki.console.terminal.commands.TCStart;
import de.mindcollaps.yuki.console.terminal.commands.TerminalCommand;
import de.mindcollaps.yuki.core.YukiSora;

import java.util.Scanner;

public class YukiTerminal {

    private static final boolean setupDone = false;

    private static TerminalCommand[] terminalCommands;

    public static void setup(YukiSora yukiSora) {
        if (setupDone)
            return;

        setupCommands(yukiSora);
        setupListener();
    }

    private static void setupCommands(YukiSora yukiSora) {
        terminalCommands = new TerminalCommand[]{
                new TCList(),
                new TCStart(yukiSora)
        };
    }

    private static void setupListener() {
        new Thread(new ConsoleCommandHandler()).start();
    }

    public static TerminalCommand[] getTerminalCommands() {
        return terminalCommands;
    }

    private static class ConsoleCommandHandler implements Runnable {

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            String line = "";
            YukiLogger.log(new YukiLogInfo("Console Command Handler initialized!", "Console Command Handler").debug());
            for (; ; ) {
                try {
                    line = scanner.nextLine();
                } catch (Exception ignored) {
                }
                try {
                    String answer = YukiTerminalCommandHandler.handleCommand(line);
                    System.out.println(answer);
                    YukiLogger.log(new YukiLogInfo("Terminal command handler answered: " + answer).debug());
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("An error occurred while executing the command!", "Console Command Handler").trace(e));
                }
            }
        }
    }
}
