# nuxeo-natural-language

**WORK IN PROGRESS - WORK IN PROGRESS - WORK IN PROGRESS**

_This "WORK IN PROGRESS" includes this README :-)_


## About / Synopsis
This plugin provides a wrapper for [Google Natural Language API](https://cloud.google.com/natural-language).

## Usage
 
## Limitations
Accepts only PLAIN_TEXT (not HTML)

## QA
TBB
 
## Requirements
Build requires the following software:
- git
- maven
 
## Build
- Enable the Natural Language API from the google developer console
- As of march 2nd 2016, billing must be activated in your google account in order to use the Vision API
- Get an API key from the Google Developer Console and set the NUXEO_GOOGLE_APPLICATION_KEY Environment Variable with the key
- Then:

```
git clone https://github.com/nuxeo-sandbox/nuxeo-natural-language
cd nuxeo-natural-language
mvn clean install
```
 
## Deploy (how to install build product)
- Install the marketplace package
- Configure a [service account](https://developers.google.com/identity/protocols/OAuth2ServiceAccount)
- Upload the JSON key file on your instance
- Edit nuxeo.conf
```
org.nuxeo.natural.language.google.credential=PATH_TO_JSON_CREDENTIAL_FILE
```
 
# Resources (Documentation and other links)
TBD

[Google Natural Language API](https://cloud.google.com/natural-language)
 
# License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
 
# About Nuxeo
The [Nuxeo Platform](http://www.nuxeo.com/products/content-management-platform/) is an open source customizable and extensible content management platform for building business applications. It provides the foundation for developing [document management](http://www.nuxeo.com/solutions/document-management/), [digital asset management](http://www.nuxeo.com/solutions/digital-asset-management/), [case management application](http://www.nuxeo.com/solutions/case-management/) and [knowledge management](http://www.nuxeo.com/solutions/advanced-knowledge-base/). You can easily add features using ready-to-use addons or by extending the platform using its extension point system.
 
The Nuxeo Platform is developed and supported by Nuxeo, with contributions from the community.
 
Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with
SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.
More information is available at [www.nuxeo.com](http://www.nuxeo.com).