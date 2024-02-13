# Spring Boot File Upload Example

This project demonstrates how to implement file upload functionality in a Spring Boot application using `MediaType.MULTIPART_FORM_DATA_VALUE`.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them:

- JDK 1.8 or later
- Maven 3.2+
- Spring Boot 2.5.0 or later (This version is just an example; use your current version)

### Installing

A step by step series of examples that tell you how to get a development environment running:

1. Clone the repository to your local machine:

```bash
git clone https://yourrepositoryurl.com/project.git

## Schema File Format

The application requires a schema file to validate the uploaded files against predefined rules. This section describes the expected format of the schema file, detailing each column in the schema.

### Schema Object Attributes

Each object within the schema file represents a column in the uploaded file, with the following attributes:

- **name**: The name of the column. This should match the field name expected in the uploaded file.
  - Type: `string`
  - Example: `"id"`

- **type**: The data type of the column. Supported types are `STRING`, `INTEGER`, `FLOAT`, `BOOLEAN`, and `DATE`.
  - Type: `string`
  - Example: `"INTEGER"`

- **excelColumn**: The corresponding column in the Excel file. This is denoted by the letter(s) representing the column in an Excel spreadsheet.
  - Type: `string`
  - Example: `"A"`

- **validation**: Specifies any validation rules that apply to the column. Common validations include `required`, which means the column must not be empty in the uploaded file.
  - Type: `string`
  - Example: `"required"`

### Example Schema File

Below is an example of a schema object within a schema file:

```json
[
  {
    "name": "id",
    "type": "INTEGER",
    "excelColumn": "A",
    "validation": "required"
  }
]
