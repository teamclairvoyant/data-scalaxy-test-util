# Steps to install [sphinx](https://www.sphinx-doc.org/en/master/) and create html files for docs

* Go to the terminal and navigate to the root directory
* Run the below commands:
```text
   python -m venv .venv
   source .venv/bin/activate
   python -m pip install --trusted-host files.pythonhosted.org --trusted-host pypi.org --trusted-host pypi.python.org -r docs/requirements.txt
   sphinx-build -b html docs/source/ docs/build/html
```
* The `build` folder should be created inside `docs` folder.
* Open `index.html` file in the browser present inside the `build/html` folder.