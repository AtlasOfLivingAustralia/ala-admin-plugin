%{--
  - Copyright (C) 2017 Atlas of Living Australia
  - All Rights Reserved.
  - The contents of this file are subject to the Mozilla Public
  - License Version 1.1 (the "License"); you may not use this file
  - except in compliance with the License. You may obtain a copy of
  - the License at http://www.mozilla.org/MPL/
  - Software distributed under the License is distributed on an "AS
  - IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
  - implied. See the License for the specific language governing
  - rights and limitations under the License.
  --}%

<div class="alert alert-info" role="alert">
    <h3>GIT info is not configured for this project</h3>
    To add GIT commit info, add the following lines to you app's <b>build.gradle</b> (after the `buildscript` block):
    <code class="git">
<pre>
plugins {
    id "com.gorylenko.gradle-git-properties" version "1.4.17"
}
</pre>
    </code>
  You may also need to modify <b>application.yml</b> to be:
  <code class="git">
<pre>
# Spring Actuator Endpoints are Disabled by Default
endpoints:
    enabled: true
</pre>
    </code>
</div>
