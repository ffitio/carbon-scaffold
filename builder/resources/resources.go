package resources

import (
	"bytes"
	"embed"
	"io"
	"io/fs"
	"os"
	"path"
)

//go:embed scaffold/**
var res embed.FS

type Module string

const (
	ModuleAdaptor        Module = "scaffold-adaptor"
	ModuleApp            Module = "scaffold-app"
	ModuleCommon         Module = "scaffold-common"
	ModuleDomain         Module = "scaffold-domain"
	ModuleInfrastructure Module = "scaffold-infrastructure"
	ModuleStarter        Module = "scaffold-starter"
)

func ModuleOf(mod string) Module {
	switch mod {
	case "adaptor":
		return ModuleAdaptor
	case "app":
		return ModuleApp
	case "common":
		return ModuleCommon
	case "domain":
		return ModuleDomain
	case "infrastructure":
		return ModuleInfrastructure
	}
	return ModuleStarter
}

const Root = "scaffold"

var getPath = func(elem ...string) string {
	return path.Join(Root, path.Join(elem...))
}

type Transfer func(src []byte) []byte

var EmptyTransfer Transfer = func(i []byte) []byte {
	return i
}

func ReadFile(filename string) ([]byte, error) {
	return res.ReadFile(getPath(filename))
}

func CopyTo(src, dst string, transfer Transfer) error {
	srcFile, err := ReadFile(src)
	if err != nil {
		return err
	}

	srcFile = transfer(srcFile)

	dstFile, err := os.Create(dst)
	if err != nil {
		return err
	}
	defer dstFile.Close()
	_, err = io.Copy(dstFile, bytes.NewReader(srcFile))
	if err != nil {
		return err
	}

	err = dstFile.Sync()
	if err != nil {
		return err
	}

	return nil
}

func Dir(name string) ([]fs.DirEntry, error) {
	return res.ReadDir(getPath(name))
}

func RootDir() ([]fs.DirEntry, error) {
	return res.ReadDir(Root)
}

func ModuleDir(mod Module) (string, []fs.DirEntry, error) {
	p := string(mod)
	d, e := Dir(p)
	return p, d, e
}
