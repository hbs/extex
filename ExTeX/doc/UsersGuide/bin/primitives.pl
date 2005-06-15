#!/bin/perl -w
##*****************************************************************************
## $Id: primitives.pl,v 1.2 2005/06/15 21:34:05 gene Exp $
##*****************************************************************************
## Author: Gerd Neugebauer
##=============================================================================

=head1 NAME

analyzeConfig - ...

=head1 SYNOPSIS

analyzeConfig [-v|--verbose] 

analyzeConfig [-h|-help]

=head1 DESCRIPTION

=head1 OPTIONS

=head1 AUTHOR

Gerd Neugebauer

=head1 BUGS

=over 4

=item *

...

=back

=cut

use strict;
use FileHandle;

#------------------------------------------------------------------------------
# Function:	usage
# Arguments:	
# Returns:	
# Description:	
#
sub usage
{ use Pod::Text;
  Pod::Text->new()->parse_from_filehandle(new FileHandle($0,'r'),\*STDERR);
}

#------------------------------------------------------------------------------
# Variable:	$verbose
# Description:	
#
my $verbose   = 0;
my $outfile   = undef;
my $pfile     = undef;
my $classpath = '.';

use Getopt::Long;
GetOptions("cp=s"		=> \$classpath,
	   "h|help"		=> \&usage,
	   "outfile=s"		=> \$outfile,
	   "primitives=s"	=> \$pfile,
	   "v|verbose"		=> \$verbose,
	  );

my %cfg	      = ();
my %primSets  = ();
my %primClass = ();
my %primitive = ();
my %primUse   = ();
my %cache     = ();

foreach my $file (@ARGV) {
  local $_ = $file;
  s|.*/(.*)\.xml|$1|;
  $cfg{$_} = $file;
  print STDERR "--- Got configuration $_ -> $file" if $verbose;
}

my $out = ($outfile ? new FileHandle($outfile, 'w'): \*STDOUT);

print $out "\n\\subsection{Predefined Configurations}\n";

foreach (sort keys %cfg) {
  processConfig($out, $_, $cfg{$_});
}

print $out "\n\\subsection{Primitive Sets}\n";

foreach (sort keys %primSets) {
  processPrimitives($out, $_, $primSets{$_});
}

$out = ($pfile ? new FileHandle($pfile, 'w'): \*STDOUT);

foreach (sort ignorecase keys %primitive) {
  processPrimitive($out, $_, $primitive{$_});
}



#------------------------------------------------------------------------------
# Function:	ignorecase
#
sub ignorecase {
  lc($a) cmp lc($b)
}

#------------------------------------------------------------------------------
# Function:	processPrimitive
#
sub processPrimitive {
  my ($out, $name, $class) = @_;
  local $_ = ($name eq ' '? '\\[': $name);

  local $_ = $class;
  s|\.|/|g;
  $_ = $classpath . '/' . $_ . '.java';
  if ($class eq 'de.dante.extex.interpreter.primitives.register.count.IntegerParameter') {
    print $out "\n\\subsection*{The Count Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a count register.\n";
  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.skip.SkipParameter') {
    print $out "\n\\subsection*{The Glue Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a skip register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.dimen.DimenParameter') {
    print $out "\n\\subsection*{The Dimen Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a dimen register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.toks.ToksParameter') {
    print $out "\n\\subsection*{The Toks Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a toks register.\n";

  } elsif ($class eq 'de.dante.extex.interpreter.primitives.register.font.NumberedFont') {
    print $out "\n\\subsection*{The Font Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is a numbered font register.\n";

  } elsif ($cache{$class}) {

  } elsif (-e $_) {
    processClass($out, $name, $_, '');
    $cache{$class} = 1;
  } else {
    print $out "\n\\subsection*{The Primitive \\Macro{$name}}\n";
    print $out "\n\\macro{$name} is not implemented yet.\n\n";
  }
  $_ = ($name eq ' '? '\\[': $name);
  print $out "The primitive \\macro{$_} is defined in the set \\texttt{$primUse{$name}}.\n";
}

#------------------------------------------------------------------------------
# Function:	translate
#
sub translate {
  local $_ = shift;
  s|^ \* ?||;
#      s|\\|\\|g;
  s|([~_\%\$])|\\$1|g;
  s|\s*\&nbsp;\s*|~|g;
  s|\&ndash;|--|g;
  s|\&lang;|\\tag{|g;
  s|\&rang;|}|g;
  s|\&\#x0*5c;|\\|g;
  s|\&rarr;|\\rightarrow{}|g;

  s|<logo>TeX</logo>|\\TeX{}|;
  s|<logo>LaTeX</logo>|\\LaTeX{}|;
  s|<logo>ExTeX</logo>|\\ExTeX{}|;
  s|<logo>eTeX</logo>|\\eTeX{}|;
  s|<logo>Omega</logo>|Omega|;
  s|<tt>\\ </tt>|\\Macro{\\[}|g;
  s|<tt>\\([^<]*)</tt>|\\Macro{$1}|g;
  s|<tt>{</tt>|\\texttt{\\char\`\\{}|g;
  s|<tt>}</tt>|\\texttt{\\char\`\\}}|g;
  s|<tt>([^<]*)</tt>|\\texttt{$1}|g;
  s|<i>([^<]*)</i>|\\textit{$1}|g;
  s|<b>([^<]*)</b>|\\emph{$1}|g;
  s|<strong>|\\emph{|g;
  s|</strong>|}|g;
  s|<em>([^<]*)</em>|\\emph{$1}|g;
  s|<sub>([^<]*)</sub>|\\ensuremath{_{$1}}|g;
  s|<sup>([^<]*)</sup>|\\ensuremath{^{$1}}|g;

  return $_;
}

#------------------------------------------------------------------------------
# Function:	processClass
#
sub processClass {
  my ($out, $name, $file, $s) = @_;
  my $fd = new FileHandle($file, 'r');
  local $_;
  my $collect = undef;

  while(<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (m|<doc.*name="([^\"]*)"|) {
      $s       = processDocTag($name, $fd, $s);
      $collect = 1;
    }
  }

  $fd->close();
  $_ = $s;
  s|\@linkplain\s+\S+\s+||sg;
  s|\@link\s+\S+\s+||sg;
  s/\\par\s+/\n\n/g;
  s/\n\n\n+/\n\n/g;
  s|\&lt;|<|g;
  s|\&gt;|>|g;

  s|\\Macro{ }|\\Macro{\\[}|g;
  s|\\Macro{\\+}|\\Macro{\\char\`\\\\}|g;
  print $out $_;
}

#------------------------------------------------------------------------------
# Function:	processDocTag
#
sub processDocTag {
  my ($name, $fd, $s) = @_;
  local $_;

  while(<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if(m|</doc>|) {
      return $s;
    }

    if ( m/ TODO / ) {
      $s .= "\n\n\\Incomplete\n\n";
      next;
    }

    $_ = translate($_);

    s|<h3>(.*)</h3>\s*|\\subsection*{$1}|;
    s|<h4>(.*)</h4>|\\subsubsection*{$1}|;
    s|^ ?Examples:\s*$|\\subsubsection*{Examples}\n|;
    s|<p>\s*||g;
    s|<dl>|\\begin{description}|g;
    s|</dl>|\\end{description}|g;
    s|<dt>|\\item[|g;
    s|</dt>|]|g;
    s|<dd>||g;
    s|</dd>||g;
    s|<p class="TeXbook">\s*|\\|g;
    s|</p>\s*|\\par |;
    s|<br[ /]*>|\\ |;
    if (m/\s*<pre\s+class="syntax">/) {
      $s .= $`;
      my $spec = '\\begin{syntax}';
      while(<$fd>) {
	$_ = translate($_) ;
	next if m/^\s*$/;
	
	s|^(\s*)\\rightarrow{}|$1\\SyntaxDef|;
	s/^(\s*)\|/$1\\SyntaxOr/;
	s/\[([a-z \<=\>\&;]*)\]/[\\texttt{$1}]/;
	
	if (m|</pre>|) {
	    $spec .= $`;
	    last;
	  }
	$spec .= $_;
      }
      $_ = $spec;
#	s|\@linkplain\s+\\([^)]+\\)\s+||sg;
#	s|\@linkplain\s+[^()]+\s+||sg;
      s|\@linkplain\s+\S+\s+||sg;
      s|\@link\s+\w+\\([^)]+\\)\s+||sg;
      s|\@link\s+[^()]+\s+||sg;
      s/\n/\t\\\\\n/mg;
      $_ .= "\n\\end{syntax}\n";

    } elsif (m/\s*<pre\s+class="JavaSample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{language=Java}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\\end{lstlisting}\n";
      
    } elsif (m/\s*<pre\s+class="TeXSample">/) {
      $s .= $`;
      my $spec = '\\begin{lstlisting}{language=TeX}' . $';
      while(<$fd>) {
	chomp;
	$_ = $_ . "\n";
	s|^ \* ?||;
#	  s|([~_\%\$])|\\$1|g;
	s|\&\#x0*5c;|\\|g;
	s|\&ndash;\s*|--|g;
	s|</?[bi]>||g;
	
	if (m|</pre>|) {
	  $spec .= $`;
	  last;
	}
	$spec .= $_;
      }
      $_ = $spec;
      $_ .= "\n\\end{lstlisting}\n";
      
    }

    print STDERR "$name: unprocessed: $&\n" if(m|</?[a-z][a-z0-9]*|i);
    $s .= $_;
  }
  return $s;
}

#------------------------------------------------------------------------------
# Function:	processPrimitives
#
sub processPrimitives {
  my ($out, $name, $file) = @_;
  my %prim = ();
  local $_;
  print $out <<__EOF__;
\\subsubsection{The Primitive Set \\texttt{$name}}

The primitive set \\texttt{$name} defines the following primitives:
\\begin{primitives}
__EOF__
  my $fd = new FileHandle($file,'r') || die "$file:$!\n";
  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (not m/>/) {
      $_ .= <$fd>;
      chomp;
      $_ = $_ . "\n";
    }
    if (m|<[dD]efine name="([^\"]*)"\s+class="([^\"]*)"|m) {
      $prim{$1}	     = $2;
      $primitive{$1} = $2;
      $primClass{$2} = $1;
      $primUse{$1}   = $name;
    }
  }
  $fd->close();
  foreach (sort ignorecase keys %prim) {
    s|\\\\|\\char`\\\\|g; #`
    #s| |\\ {}|g; #`
    $_ = "\\[" if $_ eq ' ';
    print $out "   \\macro{$_}\n";
  }
  print $out "\\end{primitives}\n";
}

#------------------------------------------------------------------------------
# Function:	processConfig
#
sub processConfig {
  my ($out, $name, $file) = @_;
  my $banner;
  local $_;
  my $extReg = undef;
  my @prim   = ();
  my $dir    = $file;
  $dir	     =~ s|/[^/]*$||;
  my $fd   = new FileHandle($file,'r');
  while (<$fd>) {
    chomp;
    $_ = $_ . "\n";
    if (m|<banner>(.*)</banner>|) {
      $banner = $1;
    } elsif (m|<ExtendedRegisterNames>(.*)</ExtendedRegisterNames>|) {
      $extReg = ($1 eq "true");
    } elsif (m|<primitives src="(.*)"|) {
      my $p = $1;
      $p =~ m|.*/(.*)\.xml|;
      $primSets{$1} = "$dir/$p";
      push @prim, $1;
    }
  }
  $fd->close();

  print $out <<__EOF__;

\\subsubsection{The Configuration \\texttt{$name}}\\index{$name}

The configuration \\texttt{$name} identifies itself as
``$banner''.
__EOF__

  $_ = @prim;
  if ($_ == 0) {
    print $out "The configuration contains no primitive sets.";
  } elsif ($_ == 1) {
    print $out "The configuration contains the primitive set \\texttt{$prim[0]}.\n";
  } elsif ($_ == 2) {
    print $out "The configuration contains the primitive sets \\texttt{$prim[0]} and \\texttt{$prim[1]}.\n";
  } else {
    print $out "The configuration contains the primitive sets ";
    my $plast = pop @prim;
    foreach (@prim) {
      print $out "\\texttt{$_}, ";
    }
    print $out " and \\texttt{$plast}.\n";
  }
  if ($extReg) {
    print $out "The configuration allows extended register names.\n";
  }
}

#------------------------------------------------------------------------------
# Local Variables: 
# mode: perl
# End: 
