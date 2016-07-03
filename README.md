# AngularJS Binding for Scala.js Showcase

This is an example implementation of [TodoMvc][todomvc], which aims to showcase typical usuage of
[scalajs-angular][scalajs-angular] library.

For more details, please refer to the documentation of the original project:

* https://github.com/greencatsoft/scalajs-angular

Most interesting parts of the project reside in the ```js``` sub-project, while the rest is a minimal
[Play][play] application which is integrated with Scala.js using Vincent Munier's
[Sbt plugin][sbt-play-scalajs].

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
[sbt-play-scalajs]: https://github.com/vmunier/sbt-play-scalajs
