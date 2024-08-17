# BPMN to Petri Net Transformation Tool

[![MTP Project](https://img.shields.io/badge/MTP-Project-blue)](https://github.com/codecasualty/MTP)

![Graph Visualization](https://miro.medium.com/max/1400/1*0yZT4pKr7lMZ1mn86dqSlA.gif)

This repository contains the code for my Major Technical Project (MTP), which focuses on transforming Business Process Model and Notation (BPMN) diagrams into Petri net models. The project was guided by Prof. Rushikesh K. Joshi, and it explores various methodologies to enhance the understanding and efficiency of business processes through graphical and computational transformations.

## Features

- **BPMN to Petri Net Conversion**: Automatically convert BPMN models into Petri nets, enabling simulation and analysis.
- **Graphical Visualization**: Generate visual representations of both BPMN and Petri net models using Graphviz.
- **Customizable Workflow**: The project supports dynamic selection of processes and visualization tools via dmenu and rofi.
- **Efficient Simulation**: Utilize Java-based algorithms to simulate Petri nets, allowing for a deeper analysis of business processes.

## Software and Tools Requirements

To successfully run this project, the following software and tools are required:

### 1. dmenu
dmenu is a dynamic menu utility for X, useful for launching scripts and applications.

- **Installation**:
    ```bash
    sudo apt install dmenu
    ```

- **Usage**:
    - dmenu reads a list of newline-separated items from stdin.
    - After the user selects an item and presses Return, the choice is printed to stdout.

### 2. Graphviz
Graphviz is an open-source graph visualization software used to represent structural information as diagrams of graphs.

- **Installation**:
    ```bash
    sudo apt install graphviz
    sudo apt install graphicsmagick-imagemagick-compat
    ```

- **Usage**:
    - Useful for generating and displaying graph-based diagrams, particularly for Petri nets.

### 3. Java
Java is a high-level, object-oriented programming language, essential for running the core algorithms of this project.

- **Installation**:
    ```bash
    sudo apt-get install openjdk-8-jdk
    ```

- **Usage**:
    - Java is used for the backend logic, including the transformation and simulation of models.

### 4. rofi
Rofi is a window switcher, application launcher, and dmenu replacement that provides a list of options for user selection.

- **Installation**:
    ```bash
    sudo apt-get install rofi
    ```

- **Usage**:
    - Similar to dmenu but with additional features for selecting windows and running scripts.

## Getting Started

After installing all the required software and tools, follow these steps to get started:

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/codecasualty/MTP.git
    cd MTP
    ```

2. **Run the Start Script**:
    - Execute the provided `start.sh` script to launch the application:
    ```bash
    ./start.sh
    ```

    This script will initialize the environment and open the user interface for BPMN to Petri net conversion and visualization.

## Visual Demonstration

![Transformation Process](https://user-images.githubusercontent.com/20465672/55122255-3f78f180-50c5-11e9-98d3-b1f68ac71d2a.gif)

## Author

- **Subodh Latkar**  
    - [GitHub](https://github.com/codecasualty)
    - [Email](mailto:mail.subodhlatkar@gmail.com)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Special thanks to **Prof. Rushikesh K. Joshi** for his guidance and support throughout this project.

---

*Transform your business processes with ease!*
