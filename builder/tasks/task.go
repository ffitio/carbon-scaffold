package tasks

type Runnable func()

type Task struct {
	runnable Runnable
}

func NewTask(runnable Runnable) *Task {
	return &Task{
		runnable: runnable,
	}
}

func (t *Task) Run() {
	t.runnable()
}
