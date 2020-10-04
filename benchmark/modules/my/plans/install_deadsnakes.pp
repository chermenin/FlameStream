plan my::install_deadsnakes() {
  $manager = get_targets('manager')
  $manager.apply_prep
  apply($manager) {
    package { 'lsb-core': }
    include apt
    apt::ppa { 'ppa:deadsnakes/ppa': }
  }
}
