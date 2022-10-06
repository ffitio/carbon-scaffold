package tasks

type Group struct {
	Name  string
	tasks []*Task
}

func NewGroup(name string) *Group {
	return &Group{
		Name:  name,
		tasks: make([]*Task, 0),
	}
}

func (g *Group) Task(task *Task) *Group {
	g.tasks = append(g.tasks, task)
	return g
}

func (g *Group) Execute() {
	for _, task := range g.tasks {
		task.Run()
	}
}
