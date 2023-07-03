project = '<repo_name>'
release = '1.0.0'

extensions = ['myst_parser']

html_theme = 'sphinx_rtd_theme'

html_theme_options = {
    'collapse_navigation': False,
    'sticky_navigation': False,
    'prev_next_buttons_location': 'both'
}

source_suffix = {
    '.rst': 'restructuredtext',
    '.txt': 'restructuredtext',
    '.md': 'markdown',
}

master_doc = 'index'

pygments_style = 'sphinx'

html_sidebars = {
    '**': [
        'about.html',
        'navigation.html',
        'relations.html',
        'searchbox.html',
        'donate.html',
    ]
}

highlight_language = 'scala'

from recommonmark.transform import AutoStructify

def setup(app):
    app.add_config_value('recommonmark_config', {
        'auto_toc_tree_section': 'Contents',
        'enable_auto_doc_ref': False
    }, True)
    app.add_transform(AutoStructify)
