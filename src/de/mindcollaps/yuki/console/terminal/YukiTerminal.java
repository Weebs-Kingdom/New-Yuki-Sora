package yuki.console.terminal;

import yuki.console.log.YukiLogInfo;
import yuki.console.log.YukiLogger;
import yuki.console.terminal.commands.TCList;
import yuki.console.terminal.commands.TCStart;
import yuki.console.terminal.commands.TerminalCommand;
import yuki.core.YukiSora;

import java.util.Scanner;

public class YukiTerminal {

    private static boolean setupDone = false;

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

    private static void setupListener(){
        new Thread(new ConsoleCommandHandler()).start();
    }

    public static TerminalCommand[] getTerminalCommands() {
        return terminalCommands;
    }

    private static class ConsoleCommandHandler implements Runnable{

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            String line = "";
            YukiLogger.log(new YukiLogInfo("Console Command Handler initialized!", "Console Command Handler").debug());
            for (;;){
                try {
                    line = scanner.nextLine();
                } catch (Exception ignored){
                }
                try {
                    System.out.println(YukiTerminalCommandHandler.handleCommand(line));
                } catch (Exception e){
                    YukiLogger.log(new YukiLogInfo("An error occurred while executing the command!", "Console Command Handler").trace(e));
                }
            }
        }
    }
}
