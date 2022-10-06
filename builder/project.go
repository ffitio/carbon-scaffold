package main

import (
	"builder/resources"
	"fmt"
	"github.com/fzdwx/infinite/components"
	"io/fs"
	"os"
	"path"
	"regexp"
	"strings"
)

type Project struct {
	*components.PrintHelper
	GroupId    string
	ArtifactId string
	Version    string
	Package    string
}

func NewProject(groupId, artifactId, version, pkg string) *Project {
	return &Project{
		GroupId:    groupId,
		ArtifactId: artifactId,
		Version:    version,
		Package:    pkg,
	}
}

func (p *Project) ReplacePom(src []byte) []byte {
	pom := string(src)

	re := regexp.MustCompile(`<groupId>io.ffit.carbon</groupId>\s*<artifactId>scaffold</artifactId>`)
	pom = re.ReplaceAllString(pom, fmt.Sprintf("<groupId>%s</groupId>\n    <artifactId>%s</artifactId>", p.GroupId, p.ArtifactId))
	pom = strings.Replace(pom, "<revision>1.0.0</revision>", fmt.Sprintf("<revision>%s</revision>", p.Version), -1)
	pom = strings.Replace(pom, "scaffold-domain", fmt.Sprintf("%s-domain", p.ArtifactId), -1)
	pom = strings.Replace(pom, "scaffold-infrastructure", fmt.Sprintf("%s-infrastructure", p.ArtifactId), -1)
	pom = strings.Replace(pom, "scaffold-app", fmt.Sprintf("%s-app", p.ArtifactId), -1)
	pom = strings.Replace(pom, "scaffold-adaptor", fmt.Sprintf("%s-adaptor", p.ArtifactId), -1)
	pom = strings.Replace(pom, "scaffold-starter", fmt.Sprintf("%s-starter", p.ArtifactId), -1)
	pom = strings.Replace(pom, "scaffold-common", fmt.Sprintf("%s-common", p.ArtifactId), -1)
	return []byte(pom)
}

func (p *Project) ReplacePackage(src []byte) []byte {
	source := string(src)
	source = strings.Replace(source, "package scaffold", fmt.Sprintf("package %s", p.Package), -1)
	source = strings.Replace(source, "import scaffold", fmt.Sprintf("import %s", p.Package), -1)
	return []byte(source)
}

func (p *Project) ReplaceMapper(src []byte) []byte {
	source := string(src)
	source = strings.Replace(source, "scaffold", p.Package, -1)
	return []byte(source)
}

func (p *Project) ModuleName(module string) string {
	return fmt.Sprintf("%s-%s", p.ArtifactId, module)
}

func (p *Project) Transfer(name string) (transfer resources.Transfer) {
	if name == "pom.xml" {
		transfer = func(src []byte) []byte {
			return p.ReplacePom(src)
		}
	} else if isJavaSource(name) {
		transfer = func(src []byte) []byte {
			return p.ReplacePackage(src)
		}
	} else if isMapper(name) {
		transfer = func(src []byte) []byte {
			return p.ReplaceMapper(src)
		}
	} else {
		transfer = resources.EmptyTransfer
	}
	return
}

func (p *Project) TransferFiles(srcPath, targetPath string, entries []fs.DirEntry, skipDir bool) error {
	err := os.Mkdir(targetPath, 0755)
	if err != nil {
		return err
	}

	for _, entry := range entries {
		name := entry.Name()
		if isIgnore(name) {
			continue
		}

		if entry.IsDir() {
			if skipDir {
				continue
			}

			subEntries, err := resources.Dir(path.Join(srcPath, name))
			if err != nil {
				return err
			}

			subTargetPath := path.Join(targetPath, name)
			if name == "scaffold" {
				subTargetPath = path.Join(targetPath, p.Package)
			}

			err = p.TransferFiles(path.Join(srcPath, name), subTargetPath, subEntries, false)
			if err != nil {
				return err
			}
		} else {
			// copy
			err := resources.CopyTo(path.Join(srcPath, entry.Name()), path.Join(targetPath, entry.Name()), p.Transfer(name))
			if err != nil {
				return err
			}
		}
	}

	return nil
}

func isJavaSource(filename string) bool {
	return path.Ext(filename) == ".java"
}

func isMapper(filename string) bool {
	return path.Ext(filename) == ".xml"
}

func isIgnore(filename string) bool {
	switch filename {
	case ".DS_Store":
		fallthrough
	case ".idea":
		fallthrough
	case ".git":
		fallthrough
	case "target":
		return true
	default:
		return false
	}
}
