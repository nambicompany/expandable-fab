---
layout: default
title: ExpandableFab JavaDoc
description: Documentation for the library in JavaDoc format for Java developers.
show_kdoc_btn: true
---

The ExpandableFab library uses Semantic Versioning for releases, in the form `MAJOR.MINOR.PATCH`. Choose the version of documentation below that corresponds to the `MAJOR` number in your ExpandableFab release.

```
NOTE: there may be components of the API shown in the documentation that you do not have access to. 
These API components will be clearly marked with the `@since [version]` block tag, where '[version]' is the release version of the ExpandableFab library that introduced that component. 
Upgrade your ExpandableFab dependency to at least [version] in order to gain access to that particular component.
```

---

{% for file in site.static_files %}
{% if file.name == "v0.javadoc" %}
  {% capture doc_link %}
  {{ site.javadoc_path | relative_url }}/{{ file.basename }}/index.html
  {% endcapture %}
  
  [{{ file.basename }}]({{ doc_link }})
{% elsif file.extname == ".javadoc" %}
  {% capture doc_link %}
  {{ site.javadoc_path | relative_url }}/{{ file.basename }}/com/nambimobile/widgets/efab/package-summary.html
  {% endcapture %}
  
  [{{ file.basename }}]({{ doc_link }})
{% endif %}
{% endfor %}