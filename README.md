<div align="center">
    <h1>CustomTableView</h1>
    <h2>CustomTableView for Android</h2>
    <p align="center">
        <p>CustomTableView is a powerful Android library for displaying complex data structures and rendering tabular data composed of rows, columns and cells. 
           CustomTableView relies on a separate model object to hold and represent the data it displays. This repository also contains a sample app that is
           designed to show you how to create your own CustomTableView in your application.</p>
    </p>
</div>

Inspired by this library
[https://github.com/evrencoskun/TableView/blob/master/README.md]

## Features

- [x] Each column width value can be calculated automatically considering the largest one.
- [x] Setting your own model class to be displayed in a table view easily.
- [x] `CustomTableView` has an action listener interface to listen user touch interaction for each cell.
- [x] `CustomTableView` columns can be sorted in ascending or descending order.
- [x] Hiding & showing the rows and columns is pretty easy.
- [x] Filtering by more than one data.
- [x] Pagination functionality.

## What's new


## Table of Contents

- [Installation](#installation)
- [Documentation](#documentation)
- [Sample Apps](#sample-apps)
- [License](#license)

## Installation

To use this library in your Android project, just add the following dependency into your module's `build.gradle`:

1. Check MavenCentral use :
```
	allprojects {
		repositories {
			...
			mavenCentral()
		}
	}
```

2. Add implementation in project build :
```
implementation 'io.github.mamomia:customtableview:1.0.0'
```

## Documentation

## Sample Apps

- This repository has a [sample application](https://github.com/mamomia/CustomTableView/tree/main/app) of `CustomTableView`.

## License

```
MIT License

Copyright (c) 2025 Musharaf Islam

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```