package tasks

type Mission struct {
	groups []*Group
}

func NewMission() *Mission {
	return &Mission{
		groups: make([]*Group, 0),
	}
}

func (m *Mission) Group(group *Group) *Mission {
	m.groups = append(m.groups, group)
	return m
}

func (m *Mission) Start() {
	for _, group := range m.groups {
		group.Execute()
	}
}
