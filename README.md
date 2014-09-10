# AngularJS Binding for Scala.js Showcase

This is an example implementation of [TodoMvc][todomvc], which aims to showcase typical usuage of [scalajs-angular][scalajs-angular] library.

For more details, please refer to the documentation of the original project:

* https://github.com/greencatsoft/scalajs-angular

Most interesting parts of the project reside in the ```scalajs``` sub-project, while the rest is a minimal [Play][play] application based on Hussachai Puripunpinyo's [example project][play-scalajs-showcase].

This project is based on, and inspired by Jokade's original contribution linked below:

* https://github.com/jokade/scalajs-angular/tree/examples/examples/todomvc

## Runing the Application

```shell
$ sbt
> run
$ open http://localhost:9000
```

[scalajs]: http://www.scala-js.org
[scalajs-angular]: https://github.com/greencatsoft/scalajs-angular
[todomvc]: http://todomvc.com
[play]: http://www.playframework.com
[play-scalajs-showcase]: https://github.com/hussachai/play-scalajs-showcase
