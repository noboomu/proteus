#!/bin/bash
  
set -e
  
command_exists() {
	command -v "$@" > /dev/null 2>&1
}


do_setup() {
	cat >&2 <<-'EOF_INIT'
	  
	EOF_INIT 
	
	sh_c='bash -c' 
	
	curl=''
	if command_exists curl; then
		curl='curl -sSL'
	elif command_exists wget; then
		curl='wget -qO-'
	fi
	
	$sh_c 'echo "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"'
}


do_setup