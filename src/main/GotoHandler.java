package main;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import inter.GotoCompletionContributor;
import inter.GotoCompletionProviderInterface;
import org.jetbrains.annotations.Nullable;
import util.GotoCompletionUtil;

import java.util.ArrayList;
import java.util.Collection;

public class GotoHandler implements GotoDeclarationHandler {
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor) {

        //fwmodify:关闭开关
//        if (!LaravelProjectComponent.isEnabled(psiElement)) {
//            return new PsiElement[0];
//        }

        Collection<PsiElement> psiTargets = new ArrayList<PsiElement>();

        PsiElement parent = psiElement.getParent();

        for(GotoCompletionContributor contributor: GotoCompletionUtil.getContributors(psiElement)) {
            GotoCompletionProviderInterface gotoCompletionContributorProvider = contributor.getProvider(psiElement);
            if(gotoCompletionContributorProvider != null) {
                // @TODO: replace this: just valid PHP files
                if(parent instanceof StringLiteralExpression) {
                    psiTargets.addAll(gotoCompletionContributorProvider.getPsiTargets((StringLiteralExpression) parent));
                } else {
                    psiTargets.addAll(gotoCompletionContributorProvider.getPsiTargets(psiElement));
                }

                psiTargets.addAll(gotoCompletionContributorProvider.getPsiTargets(psiElement, i, editor));
            }
        }
        return psiTargets.toArray(new PsiElement[psiTargets.size()]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
}