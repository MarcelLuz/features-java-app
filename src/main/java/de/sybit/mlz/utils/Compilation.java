package de.sybit.mlz.utils;

import com.google.auto.value.AutoValue;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

@AutoValue
public abstract class Compilation {

    public abstract JCTree.JCCompilationUnit compilationUnit();
    public abstract Context context();
    public abstract String source();

    private static Compilation create(
            JCTree.JCCompilationUnit compilationUnit, Context context, String source) {
        return new CompilationResult(compilationUnit, context, source);
    }

    public static Compilation compile(String fileName, String... lines) throws IOException {
        JavacTool javacTool = JavacTool.create();
        Context context = new Context();
        String source = Joiner.on("\n").join(lines);

        ImmutableList<JavaFileObject> compilationUnits =
            ImmutableList.of(
                new SimpleJavaFileObject(URI.create(fileName), JavaFileObject.Kind.SOURCE) {
                    @Override
                    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                        return source;
                    }
                });
        // disable compiler-output
        Writer writer = Writer.nullWriter();
        JavacTask task = javacTool.getTask(writer, null,
                null, null, null, compilationUnits, context);
        JCTree.JCCompilationUnit compilationUnit =
                (JCTree.JCCompilationUnit) Iterables.getOnlyElement(task.parse());
        task.analyze();
        return create(compilationUnit, context, source);
    }
}
