package installer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Installer {

    public static void main(String[] args) throws IOException, InterruptedException {

        String installDir = "C:\\Program Files\\SearchEngine\\bin";
        String exeName = "searchEngine.exe";

        Files.createDirectories(Paths.get(installDir));
        Files.copy(Paths.get(exeName), Paths.get(installDir + "\\" + exeName), StandardCopyOption.REPLACE_EXISTING);

        String cmd =
                "powershell -Command " +
                        "\"$old=[Environment]::GetEnvironmentVariable('Path','Machine');" +
                        "if($old -notlike '*" + installDir + "*'){" +
                        "[Environment]::SetEnvironmentVariable('Path', $old + ';"+installDir+"','Machine')}\"";

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        System.out.println("Successful installation.");
    }
}
