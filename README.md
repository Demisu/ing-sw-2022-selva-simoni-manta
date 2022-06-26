# Eriantys Board Game
[![License: MIT][license-image]][license]
![latest commit](https://img.shields.io/github/last-commit/Demisu/ing-sw-2022-selva-simoni-manta?color=red)
![latest release](https://img.shields.io/github/v/release/Demisu/ing-sw-2022-selva-simoni-manta?color=green)

<img src="https://i.imgur.com/PFT0MMi.png" width=192px height=192 px align="right" />

Eriantys Board Game is the final project of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2021/2022).

**Teacher** Gianpaolo Cugola

**Final Score**: N/A

## Project specification
The project consists of a Java version of the board game *Eriantys*, made by Cranio Games.
<details>
<summary><b>description</b></summary>
  
> In a world of floating islands, young magical creatures wish to enter into the great schools of magic.
>   
> Take the role of a school principal in a great challenge, what school will be the most influent? Use wisely your 10 assistants to move students into your dining room and onto the islands and guide Mother Nature on the island where you have more control, she will reward you and let you build a magic tower there!
> Be careful though! The 5 professors define who controls a faction, but they always go where the most students are. Each faction will change their minds frequently and you may lose all your support!
> 
> A light strategy family game in a cute and funny world full of magical creatures, with 12 different characters that change the rules for lots of variants!
  
</details>

You can find the full game [here](https://www.craniocreations.it/prodotto/eriantys/).

The final version includes:
* initial UML diagram;
* final UML diagram, generated from the code by automated tools;
* working game implementation, which has to be rules compliant;
* source code of the implementation;
## Usage
To play you need to have a server running and then open the client.
(both can be opened from the same jar file)
* Open a cmd (Command Prompt)
* Navigate in a folder containing the jar (use cd &lt;path&gt;)
* Use the following command
```sh
$ java -jar GC15.jar
```

## Functionalities
| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Simplified rules | 🟢 |
| Socket |🟢 |
| CLI | 🟢 |
| Complete rules | 🟢 |
| GUI | 🟢 |

| Advanced Functionality | Status |
|:-----------------------|:------------------------------------:|
| Character cards | 🟢 |
| Up-to 4 players | 🟢 |
| Multiple games | 🔴 |
| Persistence | 🔴 |
| Resilience to disconnections | 🟢 |

#### Legend
[🟢]() Implemented &nbsp;&nbsp;&nbsp;&nbsp;[🟡]() Implementing&nbsp;&nbsp;&nbsp;&nbsp;[🔴]() Not Implemented

## Test cases
All tests in model and controller have class coverage at 100%.

**Coverage criteria: code lines.**

| Package |Tested Class | Coverage |
|:-----------------------|:------------------|:------------------------------------:|
| Controller | Global Package | 86/93 (92%)
| Model | Global Package | 695/713 (97%)

## Development Team
* [Dario Simoni](https://github.com/Delath)
* [Demis Selva](https://github.com/Demisu)
* [Riccardo Manta](https://github.com/RicX8)

## Software used
**Intellij IDEA Ultimate** - main IDE 

**SceneBuilder** - GUI

**Sonarqube** - code analysis

**Gimp** - assets

## Copyright and license

Eriantys Board Game is copyrighted 2022.

Licensed under the **[MIT License](https://github.com/Demisu/ing-sw-2022-selva-simoni-manta/blob/main/LICENSE)** ;
you may not use this software except in compliance with the License.

[license]: https://github.com/Demisu/ing-sw-2022-selva-simoni-manta/blob/main/LICENSE
[license-image]: https://img.shields.io/badge/License-MIT-blue.svg
