This project is for shared code between the Play frontend and the Freeplane backend.

It should not have dependencies to Play or Freeplane.


# IDE file generation
* IntelliJ IDEA: `sbt gen-idea`
* Eclipse: `sbt eclipse`

# Packaging
* `sbt one-jar`

# Publishing
* local: `sbt publish-local`