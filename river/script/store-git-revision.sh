#
# Store the current git revision sha in a REVISION
# file accessible from the app's web root.
#

[ ! -d .git ] && echo "$0 must be run from the repository root" && exit 1

dir=src/main/webapp

git rev-parse HEAD > $dir/REVISION
