/*******************************************************************************
 * This file is part of jasima, v1.3, the Java simulator for manufacturing and 
 * logistics.
 *  
 * Copyright (c) 2015 		jasima solutions UG
 * Copyright (c) 2010-2015 Torsten Hildebrandt and jasima contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package jasima.core.experiment;

import java.util.Map;

import jasima.core.experiment.AbstractMultiExperiment.BaseExperimentCompleted;
import jasima.core.experiment.Experiment.ExpPrintEvent;
import jasima.core.experiment.Experiment.ExperimentMessage;
import jasima.core.util.observer.NotifierListener;

/**
 * This class can be used as a base class for experiment listeners. It delegates
 * all events of {@link Experiment} to separate methods.
 * 
 * @author Torsten Hildebrandt
 */
public interface ExperimentListener extends NotifierListener<Experiment, ExperimentMessage> {

	default void inform(Experiment e, ExperimentMessage event) {
		if (event == ExperimentMessage.EXPERIMENT_STARTING) {
			starting((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_INITIALIZED) {
			initialized((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_BEFORE_RUN) {
			beforeRun((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_RUN_PERFORMED) {
			runPerformed((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_AFTER_RUN) {
			afterRun((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_DONE) {
			done((Experiment) e);
		} else if (event == ExperimentMessage.EXPERIMENT_COLLECTING_RESULTS) {
			Experiment exp = (Experiment) e;
			produceResults(exp, exp.resultMap);
		} else if (event == ExperimentMessage.EXPERIMENT_FINISHING) {
			Experiment exp = (Experiment) e;
			finishing(exp, exp.resultMap);
		} else if (event == ExperimentMessage.EXPERIMENT_FINISHED) {
			Experiment exp = (Experiment) e;
			finished(exp, exp.resultMap);
		} else if (event instanceof ExpPrintEvent) {
			print((Experiment) e, (ExpPrintEvent) event);
		} else if (event instanceof BaseExperimentCompleted) {
			BaseExperimentCompleted evt = (BaseExperimentCompleted) event;
			multiExperimentCompletedTask((Experiment) e, evt.experimentRun, evt.results);
		} else {
			handleOther(e, event);
		}
	}

	default void handleOther(Experiment e, ExperimentMessage event) {
	}

	default void print(Experiment e, ExpPrintEvent event) {
	}

	default void starting(Experiment e) {
	}

	default void initialized(Experiment e) {
	}

	default void beforeRun(Experiment e) {
	}

	default void runPerformed(Experiment e) {
	}

	default void afterRun(Experiment e) {
	}

	default void done(Experiment e) {
	}

	default void produceResults(Experiment e, Map<String, Object> res) {
	}

	default void finishing(Experiment e, Map<String, Object> results) {
	}

	default void finished(Experiment e, Map<String, Object> results) {
	}

	default void multiExperimentCompletedTask(Experiment baseExp, Experiment runExperiment,
			Map<String, Object> runResults) {
	}

}
