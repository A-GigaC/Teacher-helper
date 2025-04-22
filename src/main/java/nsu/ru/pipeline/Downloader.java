package nsu.ru.pipeline;

import nsu.ru.exceptions.DSLException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Downloader {
    public static Set<String> setupStudentsRepositories(List<String> repoUrls) throws DSLException {
        Set<String> subDirs = new HashSet<>();
        File reposDir = new File("./repos/");
        if (!reposDir.exists()) {
            reposDir.mkdirs();
        }

        for (String repoUrl : repoUrls) {
            boolean success = false;
            int attempts = 3;
            Exception lastError = null;

            while (!success && attempts > 0) {
                try {
                    String[] parts = repoUrl.split("/");
                    String studentNick = parts[parts.length - 2];
                    String localRepoDirName = studentNick;
                    File repoDir = new File(reposDir, localRepoDirName);
                    subDirs.add("./repos/" + localRepoDirName);
                    // Проверяем, существует ли репозиторий
                    if (repoDir.exists() && new File(repoDir, ".git").exists()) {
                        try (Git git = Git.open(repoDir)) {
                            System.out.println("Pulling repository: " + repoDir.getName());


                            git.fetch().call();
                            git.reset().setMode(ResetCommand.ResetType.HARD).setRef("origin/main").call();
                            System.out.println("Pulled with success");
                            break;
//                            if (result.isSuccessful()) {
//                                System.out.println("Successfully pulled: " + repoDir.getName());
//                                System.out.println("Fetch result: " + result.getFetchResult());
//                                System.out.println("Merge result: " + result.getMergeResult());
//                                continue;
//                            } else {
//                                System.out.println("Unsuccess in pull for " + repoDir.getName());
//                            }
//                            success = true;

                        } catch (Exception e) {
                            System.out.println("Pull failed for: " + repoDir.getName());
                        }
                    } else if (repoDir.exists()) {
                        System.out.println("There is no repo in this folder");
                        repoDir.delete();
                    }

                    // Если репозитория нет, клонируем
                    System.out.println("Repository " + repoDir.getName() + " not found. Cloning...");
                    try (Git git = Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(repoDir)
                            .call()) {
                        System.out.println("Repo " + repoUrl + " successfully downloaded");
                        success = true;
                    }


//                    if (success) {
//                        try {
//                            encodingToUtf8(repoDir);
//                        } catch (FileNotFoundException e) {
//                            System.out.println("Encoding check failed for: " + repoDir.getName());
//                        }
//                    }
                } catch (TransportException e) {
                    attempts--;
                    lastError = e;
                    if (attempts > 0) {
                        System.out.println("Connection error. Retrying (" + attempts + " attempts left)...");
                    } else {
                        System.out.println("Failed to download repo: " + repoUrl);
                        e.printStackTrace();
                    }
                } catch (GitAPIException e) {
                    lastError = e;
                    System.out.println("Error processing repo: " + repoUrl);
                    e.printStackTrace();
                    break;
                }
            }

            if (!success && lastError != null) {
                throw new DSLException("Failed to download: " + repoUrl);
            }
        }
        return subDirs;
    }

//    private static Set<String> encodingToUtf8(File directoryRoot) throws IOException {
//
//        if (!directoryRoot.isDirectory()) {
//            FileReader fileReader = new FileReader(directoryRoot);
//            System.out.println("File encoding: " + fileReader.getEncoding());
//            fileReader.close();
//        } else {
//            File[] files = directoryRoot.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    encodingToUtf8(file);
//                }
//            }
//        }
//    }

    public static Set<String> downloadAdditional(List<String> sources) {
        Set<String> subDirs = new HashSet<>();
        int index = 0;
        for (String source: sources) {
            if (source.contains("https://github.com/")) {
                setupGithubRepo(source, "./repos/" + index);
//            } else if (source.contains("https://") || sources.contains("http://")) {
//                setupRemoteHttpResource(source, "./repos/" + index);
            } else {
                setupLocalResource(source, "./repos/" + index);
            }
            subDirs.add("./repos/" + index);
            index++;
        }
        return subDirs;
    }

    private static void setupGithubRepo(String repoUrl, String destinationPath) throws DSLException {
        File repoDir = new File(destinationPath);
        boolean success = false;
        int attempts = 3;

        while (!success && attempts > 0) {
            try {
                if (repoDir.exists() && new File(repoDir, ".git").exists()) {
                    try (Git git = Git.open(repoDir)) {
                        System.out.println("Updating repository: " + repoUrl);
                        git.fetch().call();
                        git.reset().setMode(ResetCommand.ResetType.HARD).setRef("origin/main").call();
                        System.out.println("Repository updated successfully: " + repoUrl);
                        success = true;
                    } catch (Exception e) {
                        System.err.println("[INFO] main no found. try to download master: " + repoUrl);
                        try (Git git = Git.open(repoDir)) {
                            git.reset().setMode(ResetCommand.ResetType.HARD).setRef("origin/master").call();
                            System.out.println("Repository updated successfully: " + repoUrl);
                            success = true;
                        } catch (Exception exception) {
                            attempts--;
                            System.err.println("[WARNING] Trubles when download repo: " + repoUrl);
                        }
                    }
                } else {
                    System.out.println("Cloning repository: " + repoUrl);
                    Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(repoDir)
                            .call();
                    System.out.println("Repository cloned successfully: " + repoUrl);
                    success = true;
                }
            } catch (GitAPIException e) {
                attempts--;
                System.out.println("Connection error (" + attempts + " attempts left): " + e.getMessage());
                if (attempts == 0) {
                    System.err.println("[WARNING] Trubles when download repo: " + repoUrl);
                }
            }
        }
    }

//    private static void setupRemoteHttpResource(String link, String destinationPath) {
//
//    }

    private static void setupLocalResource(String sourcePath, String destinationPath) {
        File source = new File(sourcePath);

        if (!source.exists()) {
            System.err.println("[WARNING] Cannot access given resource: " + sourcePath);
            return ;
        }

        File target = new File(destinationPath);
        try {
            if (source.isFile()) {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied: " + sourcePath);
            } else if (source.isDirectory()) {
                FileUtils.copyDirectory(source, target);
                System.out.println("Directory copied: " + sourcePath);
            }
        } catch (IOException e) {
            System.err.println("[WARNING] Failed to copy local resource: " + sourcePath);
        }
    }
}