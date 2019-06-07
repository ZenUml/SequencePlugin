package com.zenuml.testFramework.fixture;

import com.intellij.ToolExtensionPoints;
import com.intellij.analysis.AnalysisScope;
import com.intellij.codeInspection.GlobalInspectionTool;
import com.intellij.codeInspection.InspectionEP;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.deadCode.UnusedDeclarationInspection;
import com.intellij.codeInspection.deadCode.UnusedDeclarationInspectionBase;
import com.intellij.codeInspection.deadCode.UnusedDeclarationPresentation;
import com.intellij.codeInspection.ex.*;
import com.intellij.codeInspection.reference.EntryPoint;
import com.intellij.codeInspection.reference.RefElement;
import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testFramework.InspectionTestUtil;
import com.intellij.testFramework.InspectionsKt;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.GlobalInspectionContextForTests;
import com.intellij.util.containers.ContainerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InspectionTestCaseZenUml extends LightCodeInsightFixtureTestCase {
    private static final boolean MIGRATE_TEST = false;
    private static final DefaultLightProjectDescriptor ourDescriptor = new DefaultLightProjectDescriptor() {
        @Override
        public void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
            super.configureModule(module, model, contentEntry);
            contentEntry.addSourceFolder(contentEntry.getUrl() + "/ext_src", false);
            contentEntry.addSourceFolder(contentEntry.getUrl() + "/test_src", true);
        }
    };
    private EntryPoint myUnusedCodeExtension;
    private VirtualFile ext_src;

    public static GlobalInspectionToolWrapper getUnusedDeclarationWrapper() {
        InspectionEP ep = new InspectionEP();
        ep.presentation = UnusedDeclarationPresentation.class.getName();
        ep.implementationClass = UnusedDeclarationInspection.class.getName();
        ep.shortName = UnusedDeclarationInspectionBase.SHORT_NAME;
        ep.displayName = UnusedDeclarationInspectionBase.DISPLAY_NAME;
        return new GlobalInspectionToolWrapper(ep);
    }

    public InspectionManagerEx getManager() {
        return (InspectionManagerEx) InspectionManager.getInstance(getProject());
    }

    public void doTest(@NonNls @NotNull String folderName, @NotNull LocalInspectionTool tool) {
        doTest(folderName, new LocalInspectionToolWrapper(tool));
    }

    public void doTest(@NonNls @NotNull String folderName, @NotNull GlobalInspectionTool tool) {
        doTest(folderName, new GlobalInspectionToolWrapper(tool));
    }

    public void doTest(@NonNls @NotNull String folderName, @NotNull GlobalInspectionTool tool, boolean checkRange) {
        doTest(folderName, new GlobalInspectionToolWrapper(tool), checkRange);
    }

    public void doTest(@NonNls @NotNull String folderName, @NotNull GlobalInspectionTool tool, boolean checkRange, boolean runDeadCodeFirst) {
        doTest(folderName, new GlobalInspectionToolWrapper(tool), checkRange, runDeadCodeFirst);
    }

    public void doTest(@NonNls @NotNull String folderName, @NotNull InspectionToolWrapper tool) {
        doTest(folderName, tool, false);
    }

    public void doTest(@NonNls @NotNull String folderName,
                       @NotNull InspectionToolWrapper tool,
                       boolean checkRange) {
        doTest(folderName, tool, checkRange, false);
    }

    public void doTest(@NonNls @NotNull String folderName,
                       @NotNull InspectionToolWrapper toolWrapper,
                       boolean checkRange,
                       boolean runDeadCodeFirst,
                       @NotNull InspectionToolWrapper... additional) {
        final String testDir = getTestDataPath() + "/" + folderName;
        final List<InspectionToolWrapper<?, ?>> tools = getTools(runDeadCodeFirst, toolWrapper, additional);
        GlobalInspectionContextImpl context = runTool(folderName, toolWrapper, tools);

    }

    protected GlobalInspectionContextImpl runTool(@NotNull final String testName,
                                                  @NotNull InspectionToolWrapper toolWrapper,
                                                  List<? extends InspectionToolWrapper<?, ?>> tools) {
        VirtualFile projectDir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(getTestDataPath(), testName));
        assertNotNull(projectDir);

        VirtualFile srcDir;
        if (projectDir.findChild("src") != null) {
            srcDir = myFixture.copyDirectoryToProject(testName + "/src", "");
        }
        else {
            srcDir = myFixture.copyDirectoryToProject(testName,"");
        }

        if (projectDir.findChild("ext_src") != null) {
            ext_src = myFixture.copyDirectoryToProject(testName + "/ext_src", "ext_src");
        }

        if (projectDir.findChild("test_src") != null) {
            myFixture.copyDirectoryToProject(testName + "/test_src", "test_src");
        }

        AnalysisScope scope = createAnalysisScope(srcDir);

        GlobalInspectionContextForTests globalContext = InspectionsKt.createGlobalContextForTool(scope, getProject(), tools);

        InspectionTestUtil.runTool(toolWrapper, scope, globalContext);
        return globalContext;
    }

    @NotNull
    private static List<InspectionToolWrapper<?, ?>> getTools(boolean runDeadCodeFirst,
                                                              @NotNull InspectionToolWrapper toolWrapper,
                                                              @NotNull InspectionToolWrapper[] additional) {
        List<InspectionToolWrapper<?, ?>> toolWrappers = new ArrayList<>();
        if (runDeadCodeFirst) {
            toolWrappers.add(getUnusedDeclarationWrapper());
        }
        toolWrappers.add(toolWrapper);
        ContainerUtil.addAll(toolWrappers, additional);
        return toolWrappers;
    }

    @NotNull
    protected AnalysisScope createAnalysisScope(VirtualFile sourceDir) {
        PsiManager psiManager = PsiManager.getInstance(getProject());
        return new AnalysisScope(psiManager.findDirectory(sourceDir));
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return ourDescriptor;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myUnusedCodeExtension = new EntryPoint() {
            @NotNull
            @Override
            public String getDisplayName() {
                return "duh";
            }

            @Override
            public boolean isEntryPoint(@NotNull RefElement refElement, @NotNull PsiElement psiElement) {
                return isEntryPoint(psiElement);
            }

            @Override
            public boolean isEntryPoint(@NotNull PsiElement psiElement) {
                return ext_src != null && VfsUtilCore.isAncestor(ext_src, PsiUtilCore.getVirtualFile(psiElement), false);
            }

            @Override
            public boolean isSelected() {
                return true;
            }

            @Override
            public void setSelected(boolean selected) {
            }

            @Override
            public void readExternal(Element element) {
            }

            @Override
            public void writeExternal(Element element) {
            }
        };

    }

    @Override
    protected void tearDown() throws Exception {
        try {
            myUnusedCodeExtension = null;
            ext_src = null;
            super.tearDown();
        }
        catch (Throwable e) {
            addSuppressedException(e);
        }
    }

    @Override
    @NonNls
    protected String getTestDataPath() {
        return PathManagerEx.getTestDataPath() + "/inspection/";
    }

}
