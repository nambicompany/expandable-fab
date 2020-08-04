# How to test locally
https://docs.github.com/en/github/working-with-github-pages/testing-your-github-pages-site-locally-with-jekyll

If using Bundler: bundle exec jekyll serve --baseurl ''


# General Jekyll/Liquid/GitHub Pages docs
https://jekyllrb.com/docs/pages/
https://docs.github.com/en/github/working-with-github-pages/adding-content-to-your-github-pages-site-using-jekyll


# Create new gifs of library functionality
* First trim the video (Windows Photos app has trim feature under for mp4s under 'Edit & Create')
* Use https://ezgif.com/video-to-gif to convert video to gif (other options, including built in Samsung functionality, come out too blurry)
    * Select max frame rate available when converting to gif
* Use https://ezgif.com/resize to resize the gif
    * Set percentage to 45% in order to match dimensions of gifs already in the gallery folder
* Save it and you're set!


# General Notes
* CSS from the cayman theme is overridden in _sass/jekyll-theme-cayman.scss. Specifically, we added 'max-height: [some value];' to the '.highlight pre, pre' rule set. This prevents code blocks from becoming too long when containing large code snippets. The blocks will now introduce a scrollbar if the content goes beyond the value set in 'max-height'.