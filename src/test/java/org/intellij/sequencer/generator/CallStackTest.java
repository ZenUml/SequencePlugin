package org.intellij.sequencer.generator;

import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.IncorrectOperationException;
import org.intellij.sequencer.config.ExcludeEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CallStackTest {

    private PsiFile psiFile;
    private PsiClass psiClass;
    private PsiMethod psiMethod;

    @Test
    public void test() {
        List<ExcludeEntry> excludeList = new ArrayList<>();
        SequenceParams params = new SequenceParams(excludeList);
        SequenceGenerator sequenceGenerator = new SequenceGenerator(params);
        psiFile = getPsiFile();
        psiClass = getPsiClass();
        psiMethod = getPsiMethod();
        CallStack callStack = sequenceGenerator.generate(psiMethod);
        System.out.println(callStack);
    }

    @NotNull
    private PsiMethod getPsiMethod() {
        return new PsiMethod() {
            @Nullable
            @Override
            public <T> T getUserData(@NotNull Key<T> key) {
                return null;
            }

            @Override
            public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

            }

            @Override
            public Icon getIcon(int flags) {
                return null;
            }

            @NotNull
            @Override
            public Project getProject() throws PsiInvalidElementAccessException {
                return null;
            }

            @NotNull
            @Override
            public Language getLanguage() {
                return null;
            }

            @Override
            public PsiManager getManager() {
                return null;
            }

            @NotNull
            @Override
            public PsiElement[] getChildren() {
                return new PsiElement[0];
            }

            @Override
            public PsiElement getParent() {
                return null;
            }

            @Override
            public PsiElement getFirstChild() {
                return null;
            }

            @Override
            public PsiElement getLastChild() {
                return null;
            }

            @Override
            public PsiElement getNextSibling() {
                return null;
            }

            @Override
            public PsiElement getPrevSibling() {
                return null;
            }

            @Override
            public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
                return psiFile;
            }

            @Override
            public TextRange getTextRange() {
                return null;
            }

            @Override
            public int getStartOffsetInParent() {
                return 0;
            }

            @Override
            public int getTextLength() {
                return 0;
            }

            @Nullable
            @Override
            public PsiElement findElementAt(int offset) {
                return null;
            }

            @Nullable
            @Override
            public PsiReference findReferenceAt(int offset) {
                return null;
            }

            @Override
            public int getTextOffset() {
                return 0;
            }

            @Override
            public String getText() {
                return null;
            }

            @NotNull
            @Override
            public char[] textToCharArray() {
                return new char[0];
            }

            @Override
            public PsiElement getNavigationElement() {
                return null;
            }

            @Override
            public PsiElement getOriginalElement() {
                return null;
            }

            @Override
            public boolean textMatches(@NotNull CharSequence text) {
                return false;
            }

            @Override
            public boolean textMatches(@NotNull PsiElement element) {
                return false;
            }

            @Override
            public boolean textContains(char c) {
                return false;
            }

            @Override
            public void accept(@NotNull PsiElementVisitor visitor) {

            }

            @Override
            public void acceptChildren(@NotNull PsiElementVisitor visitor) {

            }

            @Override
            public PsiElement copy() {
                return null;
            }

            @Override
            public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {

            }

            @Override
            public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void delete() throws IncorrectOperationException {

            }

            @Override
            public void checkDelete() throws IncorrectOperationException {

            }

            @Override
            public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {

            }

            @Override
            public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
                return null;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            @Nullable
            @Override
            public PsiReference getReference() {
                return null;
            }

            @NotNull
            @Override
            public PsiReference[] getReferences() {
                return new PsiReference[0];
            }

            @Nullable
            @Override
            public <T> T getCopyableUserData(Key<T> key) {
                return null;
            }

            @Override
            public <T> void putCopyableUserData(Key<T> key, @Nullable T value) {

            }

            @Override
            public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
                return false;
            }

            @Nullable
            @Override
            public PsiElement getContext() {
                return null;
            }

            @Override
            public boolean isPhysical() {
                return false;
            }

            @NotNull
            @Override
            public GlobalSearchScope getResolveScope() {
                return null;
            }

            @NotNull
            @Override
            public SearchScope getUseScope() {
                return null;
            }

            @Override
            public ASTNode getNode() {
                return null;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public boolean isEquivalentTo(PsiElement another) {
                return false;
            }

            @Override
            public void navigate(boolean requestFocus) {

            }

            @Override
            public boolean canNavigate() {
                return false;
            }

            @Override
            public boolean canNavigateToSource() {
                return false;
            }

            @Nullable
            @Override
            public PsiClass getContainingClass() {
                return psiClass;
            }

            @Override
            public boolean hasTypeParameters() {
                return false;
            }

            @Nullable
            @Override
            public PsiTypeParameterList getTypeParameterList() {
                return null;
            }

            @NotNull
            @Override
            public PsiTypeParameter[] getTypeParameters() {
                return new PsiTypeParameter[0];
            }

            @Nullable
            @Override
            public PsiDocComment getDocComment() {
                return null;
            }

            @Override
            public boolean isDeprecated() {
                return false;
            }

            @Nullable
            @Override
            public PsiType getReturnType() {
                return null;
            }

            @Nullable
            @Override
            public PsiTypeElement getReturnTypeElement() {
                return null;
            }

            @NotNull
            @Override
            public PsiParameterList getParameterList() {
                return null;
            }

            @NotNull
            @Override
            public PsiReferenceList getThrowsList() {
                return null;
            }

            @Nullable
            @Override
            public PsiCodeBlock getBody() {
                return null;
            }

            @Override
            public boolean isConstructor() {
                return false;
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @NotNull
            @Override
            public MethodSignature getSignature(@NotNull PsiSubstitutor substitutor) {
                return null;
            }

            @Nullable
            @Override
            public PsiIdentifier getNameIdentifier() {
                return null;
            }

            @NotNull
            @Override
            public PsiMethod[] findSuperMethods() {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiMethod[] findSuperMethods(boolean checkAccess) {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiMethod[] findSuperMethods(PsiClass parentClass) {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public List<MethodSignatureBackedByPsiMethod> findSuperMethodSignaturesIncludingStatic(boolean checkAccess) {
                return null;
            }

            @Nullable
            @Override
            public PsiMethod findDeepestSuperMethod() {
                return null;
            }

            @NotNull
            @Override
            public PsiMethod[] findDeepestSuperMethods() {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiModifierList getModifierList() {
                return null;
            }

            @Override
            public boolean hasModifierProperty(@NotNull String name) {
                return false;
            }

            @NotNull
            @Override
            public String getName() {
                return null;
            }

            @Nullable
            @Override
            public ItemPresentation getPresentation() {
                return null;
            }

            @Override
            public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
                return null;
            }

            @NotNull
            @Override
            public HierarchicalMethodSignature getHierarchicalMethodSignature() {
                return null;
            }
        };
    }

    @NotNull
    private PsiClass getPsiClass() {
        return new PsiClass() {
            @Nullable
            @Override
            public String getQualifiedName() {
                return null;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isAnnotationType() {
                return false;
            }

            @Override
            public boolean isEnum() {
                return false;
            }

            @Nullable
            @Override
            public PsiReferenceList getExtendsList() {
                return null;
            }

            @Nullable
            @Override
            public PsiReferenceList getImplementsList() {
                return null;
            }

            @NotNull
            @Override
            public PsiClassType[] getExtendsListTypes() {
                return new PsiClassType[0];
            }

            @NotNull
            @Override
            public PsiClassType[] getImplementsListTypes() {
                return new PsiClassType[0];
            }

            @Nullable
            @Override
            public PsiClass getSuperClass() {
                return null;
            }

            @Override
            public PsiClass[] getInterfaces() {
                return new PsiClass[0];
            }

            @NotNull
            @Override
            public PsiClass[] getSupers() {
                return new PsiClass[0];
            }

            @NotNull
            @Override
            public PsiClassType[] getSuperTypes() {
                return new PsiClassType[0];
            }

            @NotNull
            @Override
            public PsiField[] getFields() {
                return new PsiField[0];
            }

            @NotNull
            @Override
            public PsiMethod[] getMethods() {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiMethod[] getConstructors() {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiClass[] getInnerClasses() {
                return new PsiClass[0];
            }

            @NotNull
            @Override
            public PsiClassInitializer[] getInitializers() {
                return new PsiClassInitializer[0];
            }

            @NotNull
            @Override
            public PsiField[] getAllFields() {
                return new PsiField[0];
            }

            @NotNull
            @Override
            public PsiMethod[] getAllMethods() {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiClass[] getAllInnerClasses() {
                return new PsiClass[0];
            }

            @Nullable
            @Override
            public PsiField findFieldByName(String name, boolean checkBases) {
                return null;
            }

            @Nullable
            @Override
            public PsiMethod findMethodBySignature(PsiMethod patternMethod, boolean checkBases) {
                return null;
            }

            @NotNull
            @Override
            public PsiMethod[] findMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public PsiMethod[] findMethodsByName(String name, boolean checkBases) {
                return new PsiMethod[0];
            }

            @NotNull
            @Override
            public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(String name, boolean checkBases) {
                return null;
            }

            @NotNull
            @Override
            public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
                return null;
            }

            @Nullable
            @Override
            public PsiClass findInnerClassByName(String name, boolean checkBases) {
                return null;
            }

            @Nullable
            @Override
            public PsiElement getLBrace() {
                return null;
            }

            @Nullable
            @Override
            public PsiElement getRBrace() {
                return null;
            }

            @Nullable
            @Override
            public PsiIdentifier getNameIdentifier() {
                return null;
            }

            @Override
            public PsiElement getScope() {
                return null;
            }

            @Override
            public boolean isInheritor(@NotNull PsiClass baseClass, boolean checkDeep) {
                return false;
            }

            @Override
            public boolean isInheritorDeep(PsiClass baseClass, @Nullable PsiClass classToByPass) {
                return false;
            }

            @Nullable
            @Override
            public PsiClass getContainingClass() {
                return null;
            }

            @NotNull
            @Override
            public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
                return null;
            }

            @Override
            public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
                return null;
            }

            @Nullable
            @Override
            public PsiDocComment getDocComment() {
                return null;
            }

            @Override
            public boolean isDeprecated() {
                return false;
            }

            @Override
            public boolean hasTypeParameters() {
                return false;
            }

            @Nullable
            @Override
            public PsiTypeParameterList getTypeParameterList() {
                return null;
            }

            @NotNull
            @Override
            public PsiTypeParameter[] getTypeParameters() {
                return new PsiTypeParameter[0];
            }

            @Nullable
            @Override
            public ItemPresentation getPresentation() {
                return null;
            }

            @Override
            public void navigate(boolean requestFocus) {

            }

            @Override
            public boolean canNavigate() {
                return false;
            }

            @Override
            public boolean canNavigateToSource() {
                return false;
            }

            @Nullable
            @Override
            public PsiModifierList getModifierList() {
                return null;
            }

            @Override
            public boolean hasModifierProperty(@NotNull String name) {
                return false;
            }

            @Nullable
            @Override
            public String getName() {
                return null;
            }

            @NotNull
            @Override
            public Project getProject() throws PsiInvalidElementAccessException {
                return null;
            }

            @NotNull
            @Override
            public Language getLanguage() {
                return null;
            }

            @Override
            public PsiManager getManager() {
                return null;
            }

            @NotNull
            @Override
            public PsiElement[] getChildren() {
                return new PsiElement[0];
            }

            @Override
            public PsiElement getParent() {
                return null;
            }

            @Override
            public PsiElement getFirstChild() {
                return null;
            }

            @Override
            public PsiElement getLastChild() {
                return null;
            }

            @Override
            public PsiElement getNextSibling() {
                return null;
            }

            @Override
            public PsiElement getPrevSibling() {
                return null;
            }

            @Override
            public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
                return psiFile;
            }

            @Override
            public TextRange getTextRange() {
                return null;
            }

            @Override
            public int getStartOffsetInParent() {
                return 0;
            }

            @Override
            public int getTextLength() {
                return 0;
            }

            @Nullable
            @Override
            public PsiElement findElementAt(int offset) {
                return null;
            }

            @Nullable
            @Override
            public PsiReference findReferenceAt(int offset) {
                return null;
            }

            @Override
            public int getTextOffset() {
                return 0;
            }

            @Override
            public String getText() {
                return null;
            }

            @NotNull
            @Override
            public char[] textToCharArray() {
                return new char[0];
            }

            @Override
            public PsiElement getNavigationElement() {
                return null;
            }

            @Override
            public PsiElement getOriginalElement() {
                return null;
            }

            @Override
            public boolean textMatches(@NotNull CharSequence text) {
                return false;
            }

            @Override
            public boolean textMatches(@NotNull PsiElement element) {
                return false;
            }

            @Override
            public boolean textContains(char c) {
                return false;
            }

            @Override
            public void accept(@NotNull PsiElementVisitor visitor) {
                visitor.visitElement(this);
            }

            @Override
            public void acceptChildren(@NotNull PsiElementVisitor visitor) {

            }

            @Override
            public PsiElement copy() {
                return null;
            }

            @Override
            public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {

            }

            @Override
            public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void delete() throws IncorrectOperationException {

            }

            @Override
            public void checkDelete() throws IncorrectOperationException {

            }

            @Override
            public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {

            }

            @Override
            public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
                return null;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            @Nullable
            @Override
            public PsiReference getReference() {
                return null;
            }

            @NotNull
            @Override
            public PsiReference[] getReferences() {
                return new PsiReference[0];
            }

            @Nullable
            @Override
            public <T> T getCopyableUserData(Key<T> key) {
                return null;
            }

            @Override
            public <T> void putCopyableUserData(Key<T> key, @Nullable T value) {

            }

            @Override
            public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
                return false;
            }

            @Nullable
            @Override
            public PsiElement getContext() {
                return null;
            }

            @Override
            public boolean isPhysical() {
                return false;
            }

            @NotNull
            @Override
            public GlobalSearchScope getResolveScope() {
                return null;
            }

            @NotNull
            @Override
            public SearchScope getUseScope() {
                return null;
            }

            @Override
            public ASTNode getNode() {
                return null;
            }

            @Override
            public boolean isEquivalentTo(PsiElement another) {
                return false;
            }

            @Override
            public Icon getIcon(int flags) {
                return null;
            }

            @Nullable
            @Override
            public <T> T getUserData(@NotNull Key<T> key) {
                return null;
            }

            @Override
            public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

            }
        };
    }

    @NotNull
    private PsiFile getPsiFile() {
        return new PsiFile() {
            @Override
            public VirtualFile getVirtualFile() {
                return null;
            }

            @Override
            public PsiDirectory getContainingDirectory() {
                return null;
            }

            @Override
            public PsiDirectory getParent() {
                return null;
            }

            @Override
            public long getModificationStamp() {
                return 0;
            }

            @NotNull
            @Override
            public PsiFile getOriginalFile() {
                return null;
            }

            @NotNull
            @Override
            public FileType getFileType() {
                return null;
            }

            @NotNull
            @Override
            public PsiFile[] getPsiRoots() {
                return new PsiFile[0];
            }

            @NotNull
            @Override
            public FileViewProvider getViewProvider() {
                return null;
            }

            @Override
            public FileASTNode getNode() {
                return null;
            }

            @Override
            public void subtreeChanged() {

            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @NotNull
            @Override
            public String getName() {
                return "A.class";
            }

            @Override
            public boolean processChildren(PsiElementProcessor<PsiFileSystemItem> processor) {
                return false;
            }

            @Nullable
            @Override
            public ItemPresentation getPresentation() {
                return null;
            }

            @Override
            public void navigate(boolean requestFocus) {

            }

            @Override
            public boolean canNavigate() {
                return false;
            }

            @Override
            public boolean canNavigateToSource() {
                return false;
            }

            @Override
            public void checkSetName(String name) throws IncorrectOperationException {

            }

            @Override
            public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
                return null;
            }

            @NotNull
            @Override
            public Project getProject() throws PsiInvalidElementAccessException {
                return null;
            }

            @NotNull
            @Override
            public Language getLanguage() {
                return null;
            }

            @Override
            public PsiManager getManager() {
                return null;
            }

            @NotNull
            @Override
            public PsiElement[] getChildren() {
                return new PsiElement[0];
            }

            @Override
            public PsiElement getFirstChild() {
                return null;
            }

            @Override
            public PsiElement getLastChild() {
                return null;
            }

            @Override
            public PsiElement getNextSibling() {
                return null;
            }

            @Override
            public PsiElement getPrevSibling() {
                return null;
            }

            @Override
            public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
                return this;
            }

            @Override
            public TextRange getTextRange() {
                return null;
            }

            @Override
            public int getStartOffsetInParent() {
                return 0;
            }

            @Override
            public int getTextLength() {
                return 0;
            }

            @Nullable
            @Override
            public PsiElement findElementAt(int offset) {
                return null;
            }

            @Nullable
            @Override
            public PsiReference findReferenceAt(int offset) {
                return null;
            }

            @Override
            public int getTextOffset() {
                return 0;
            }

            @Override
            public String getText() {
                return null;
            }

            @NotNull
            @Override
            public char[] textToCharArray() {
                return new char[0];
            }

            @Override
            public PsiElement getNavigationElement() {
                return null;
            }

            @Override
            public PsiElement getOriginalElement() {
                return null;
            }

            @Override
            public boolean textMatches(@NotNull CharSequence text) {
                return false;
            }

            @Override
            public boolean textMatches(@NotNull PsiElement element) {
                return false;
            }

            @Override
            public boolean textContains(char c) {
                return false;
            }

            @Override
            public void accept(@NotNull PsiElementVisitor visitor) {

            }

            @Override
            public void acceptChildren(@NotNull PsiElementVisitor visitor) {

            }

            @Override
            public PsiElement copy() {
                return null;
            }

            @Override
            public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {

            }

            @Override
            public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
                return null;
            }

            @Override
            public void delete() throws IncorrectOperationException {

            }

            @Override
            public void checkDelete() throws IncorrectOperationException {

            }

            @Override
            public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {

            }

            @Override
            public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
                return null;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            @Nullable
            @Override
            public PsiReference getReference() {
                return null;
            }

            @NotNull
            @Override
            public PsiReference[] getReferences() {
                return new PsiReference[0];
            }

            @Nullable
            @Override
            public <T> T getCopyableUserData(Key<T> key) {
                return null;
            }

            @Override
            public <T> void putCopyableUserData(Key<T> key, @Nullable T value) {

            }

            @Override
            public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
                return false;
            }

            @Nullable
            @Override
            public PsiElement getContext() {
                return null;
            }

            @Override
            public boolean isPhysical() {
                return false;
            }

            @NotNull
            @Override
            public GlobalSearchScope getResolveScope() {
                return null;
            }

            @NotNull
            @Override
            public SearchScope getUseScope() {
                return null;
            }

            @Override
            public boolean isEquivalentTo(PsiElement another) {
                return false;
            }

            @Override
            public Icon getIcon(int flags) {
                return null;
            }

            @Nullable
            @Override
            public <T> T getUserData(@NotNull Key<T> key) {
                return null;
            }

            @Override
            public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

            }
        };
    }
}