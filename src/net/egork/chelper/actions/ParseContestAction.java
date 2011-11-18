package net.egork.chelper.actions;

import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import net.egork.chelper.configurations.TaskConfiguration;
import net.egork.chelper.configurations.TaskConfigurationType;
import net.egork.chelper.parser.ContestParser;
import net.egork.chelper.parser.codeforces.CodeforcesContestParser;
import net.egork.chelper.task.Task;
import net.egork.chelper.ui.ParseDialog;
import net.egork.chelper.util.Utilities;

import java.util.Collection;

/**
 * @author Egor Kulikov (kulikov@devexperts.com)
 */
public class ParseContestAction extends AnAction {
	public static final ContestParser[] PARSERS = {CodeforcesContestParser.INSTANCE};

	public void actionPerformed(AnActionEvent e) {
		if (!Utilities.isEligible(e.getDataContext()))
			return;
		Project project = Utilities.getProject(e.getDataContext());
		RunManagerImpl manager = RunManagerImpl.getInstanceImpl(project);
		Collection<Task> tasks = ParseDialog.parseContest(project);
		RunnerAndConfigurationSettingsImpl firstConfiguration = null;
		PsiElement firstElement = null;
		for (Task task : tasks) {
			PsiElement element = task.initialize();
			RunnerAndConfigurationSettingsImpl configuration = new RunnerAndConfigurationSettingsImpl(manager,
				new TaskConfiguration(task.name, project, task,
				TaskConfigurationType.INSTANCE.getConfigurationFactories()[0]), false);
			manager.addConfiguration(configuration, false);
			if (firstConfiguration == null)
				firstConfiguration = configuration;
			if (firstElement == null)
				firstElement = element;
		}
		if (firstConfiguration != null)
			manager.setActiveConfiguration(firstConfiguration);
		if (firstElement != null)
			Utilities.openElement(project, firstElement);
	}

}