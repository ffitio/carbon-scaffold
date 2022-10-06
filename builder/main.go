package main

import (
	"builder/components/text"
	"builder/resources"
	"builder/tasks"
	"fmt"
	inf "github.com/fzdwx/infinite"
	"github.com/fzdwx/infinite/color"
	"github.com/fzdwx/infinite/components/spinner"
	"github.com/fzdwx/infinite/style"
	"github.com/fzdwx/infinite/theme"
	"os"
	"path"
	"strings"
	"time"
)

const (
	Adaptor        = "adaptor"
	App            = "app"
	Common         = "common"
	Domain         = "domain"
	Infrastructure = "infrastructure"
	Starter        = "starter"
)

func main() {
	inf.NewSpinner(
		spinner.WithPrompt(" Starting..."),
		spinner.WithDisableOutputResult(),
	).Display(func(spinner *spinner.Spinner) {
		time.Sleep(time.Millisecond * 1000)
		spinner.PrintWithPrefix(style.New().Fg(color.Green).Render("READY "), "Carbon Scaffold Builder Ver:%s", "1.0.0")
	})

	txtGroupId := text.New(
		text.WithPrompt("groupId"),
		text.WithPromptStyle(theme.DefaultTheme.PromptStyle),
		text.WithDefaultValue("io.ffit.galaxy"),
	)

	groupId, _ := txtGroupId.Display()

	txtArtifactId := text.New(
		text.WithRequired(),
		text.WithRequiredMsg("artifactId cannot be empty"),
		text.WithPrompt("artifactId"),
		text.WithPromptStyle(theme.DefaultTheme.PromptStyle),
	)

	artifactId, _ := txtArtifactId.Display()

	txtVersion := text.New(
		text.WithPrompt("version"),
		text.WithPromptStyle(theme.DefaultTheme.PromptStyle),
		text.WithDefaultValue("1.0.0"),
	)

	version, _ := txtVersion.Display()

	txtPackage := text.New(
		text.WithPrompt("package"),
		text.WithPromptStyle(theme.DefaultTheme.PromptStyle),
		text.WithDefaultValue(fmt.Sprintf("%s.%s", groupId, strings.Replace(artifactId, "-", ".", -1))),
	)

	pkg, _ := txtPackage.Display()

	project := NewProject(groupId, artifactId, version, pkg)
	projectPath := path.Join("./", artifactId)

	if pathExists(projectPath) {
		if !isDir(projectPath) {
			project.Error("project path is not a directory")
			return
		}
	}

	// create project
	inf.NewSpinner(
		spinner.WithPrompt(" Creating Project..."),
		spinner.WithDisableOutputResult(),
	).Display(func(spinner *spinner.Spinner) {
		createTask := func(mod string) *tasks.Task {
			return tasks.NewTask(func() {
				spinner.Refreshf(" Creating Module %s...", project.ModuleName(mod))
				p, entries, err := resources.ModuleDir(resources.ModuleOf(mod))
				if err != nil {
					return
				}

				_ = project.TransferFiles(p, path.Join(projectPath, project.ModuleName(mod)), entries, false)
			})
		}

		entries, err := resources.RootDir()
		if err != nil {
			spinner.Error("", err)
		}
		err = project.TransferFiles("", projectPath, entries, true)
		if err != nil {
			spinner.Error("", err)
		}

		mission := tasks.NewMission()
		mission.Group(
			tasks.NewGroup(project.ModuleName(Adaptor)).Task(createTask(Adaptor)),
		).Group(
			tasks.NewGroup(project.ModuleName(App)).Task(createTask(App)),
		).Group(
			tasks.NewGroup(project.ModuleName(Common)).Task(createTask(Common)),
		).Group(
			tasks.NewGroup(project.ModuleName(Domain)).Task(createTask(Domain)),
		).Group(
			tasks.NewGroup(project.ModuleName(Infrastructure)).Task(createTask(Infrastructure)),
		).Group(
			tasks.NewGroup(project.ModuleName(Starter)).Task(createTask(Starter)),
		).Start()

		spinner.Success("Project Created")
	})
}

func pathExists(path string) bool {
	_, err := os.Stat(path)
	if err != nil {
		if os.IsExist(err) {
			return true
		}
		return false
	}
	return true
}

func isDir(path string) bool {
	s, err := os.Stat(path)
	if err != nil {
		return false
	}
	return s.IsDir()
}
