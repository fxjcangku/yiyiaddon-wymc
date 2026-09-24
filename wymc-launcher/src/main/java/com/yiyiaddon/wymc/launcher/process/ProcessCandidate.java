package com.yiyiaddon.wymc.launcher.process;

import java.util.*;

public class ProcessCandidate {
    private final long pid;
    private final String executable;
    private final String commandLine;
    private final String workingDirectory;
    private final String javaHome;
    private final String javaVersion;
    private final String classpath;
    private final Map<String, String> evidence;
    private int score;
    
    public ProcessCandidate(long pid) {
        this.pid = pid;
        this.executable = "";
        this.commandLine = "";
        this.workingDirectory = "";
        this.javaHome = "";
        this.javaVersion = "";
        this.classpath = "";
        this.evidence = new HashMap<>();
        this.score = 0;
    }
    
    private ProcessCandidate(Builder builder) {
        this.pid = builder.pid;
        this.executable = builder.executable;
        this.commandLine = builder.commandLine;
        this.workingDirectory = builder.workingDirectory;
        this.javaHome = builder.javaHome;
        this.javaVersion = builder.javaVersion;
        this.classpath = builder.classpath;
        this.evidence = new HashMap<>(builder.evidence);
        this.score = builder.score;
    }
    
    public long getPid() { return pid; }
    public String getExecutable() { return executable; }
    public String getCommandLine() { return commandLine; }
    public String getWorkingDirectory() { return workingDirectory; }
    public String getJavaHome() { return javaHome; }
    public String getJavaVersion() { return javaVersion; }
    public String getClasspath() { return classpath; }
    public Map<String, String> getEvidence() { return Collections.unmodifiableMap(evidence); }
    public int getScore() { return score; }
    
    public static class Builder {
        private final long pid;
        private String executable = "";
        private String commandLine = "";
        private String workingDirectory = "";
        private String javaHome = "";
        private String javaVersion = "";
        private String classpath = "";
        private Map<String, String> evidence = new HashMap<>();
        private int score = 0;
        
        public Builder(long pid) {
            this.pid = pid;
        }
        
        public Builder executable(String executable) {
            this.executable = executable != null ? executable : "";
            return this;
        }
        
        public Builder commandLine(String commandLine) {
            this.commandLine = commandLine != null ? commandLine : "";
            return this;
        }
        
        public Builder workingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory != null ? workingDirectory : "";
            return this;
        }
        
        public Builder javaHome(String javaHome) {
            this.javaHome = javaHome != null ? javaHome : "";
            return this;
        }
        
        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion != null ? javaVersion : "";
            return this;
        }
        
        public Builder classpath(String classpath) {
            this.classpath = classpath != null ? classpath : "";
            return this;
        }
        
        public Builder addEvidence(String key, String value) {
            this.evidence.put(key, value);
            return this;
        }
        
        public Builder score(int score) {
            this.score = score;
            return this;
        }
        
        public ProcessCandidate build() {
            return new ProcessCandidate(this);
        }
    }
}
