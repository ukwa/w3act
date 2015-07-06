# W3ACT Release Process
Documented below is the process for releasing new versions of W3ACT. 

1. Inform userbase of pending release of new W3ACT code

 The actual release time isn't long but the users should receive a day's grace to complete all work in progress.

2. Build the new W3ACT release

 Before building the new release, ensure the current, shortly to become previous, version is backed up so that, if necessary, it can be rolled back to. 
 
 The W3ACT service should be built as a 'play dist' version. Clone recursively the github w3act repo; amend the conf settings (in particular the service passwords); and build with 'play dist'. Then unroll this version onto the production server, amending any necessary symlinks. The 'GeoLite2-City.mmdb' and 'last-version.txt' files should be copied from the build directory to the production location. This is a good moment to rotate the service logs too, before starting the new service.
 
3. Communicate the completion of the new release

 It can also be helpful to inform the userbase of the enhancements, changes and bug fixes included in this new release.
 
4. Tag the release

 The released code should be tagged in github.
