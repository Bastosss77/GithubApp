# GithubApp

## What it does ?

- Fetch all public repositories
- Search repository
- Provide informations on specific repository (contributors, branches, issues, pull requests)

## Architecture

- /common : Contains utils files and widgets used in the whole app
- /core : Contains main files
    - /appmodel : Contains classes used in views and used to manipulate datas
    - /githubmodel : Contains classes wich match with response of Github REST api
    - /provider : Classes used to request datas from api
    - /service : List of endpoints from the api
- /home : Package contains home's view
- /repository: Package contains view wich shows repository's datas
    
